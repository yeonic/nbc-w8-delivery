package com.ateen.delivery.domain.menu.service;

import com.ateen.delivery.domain.menu.dto.request.MenuSaveRequest;
import com.ateen.delivery.domain.menu.dto.request.MenuUpdateRequest;
import com.ateen.delivery.domain.menu.dto.response.MenuResponse;
import com.ateen.delivery.domain.menu.dto.response.MenuSaveResponse;
import com.ateen.delivery.domain.menu.dto.response.MenuUpdateResponse;
import com.ateen.delivery.domain.menu.entity.Menu;
import com.ateen.delivery.domain.menu.repository.MenuRepository;

import com.ateen.delivery.domain.store.entity.Store;
import com.ateen.delivery.domain.store.repository.StoreRepository;
import com.ateen.delivery.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public MenuSaveResponse save(Long ownerId, Long storeId, MenuSaveRequest request) {

        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 가게를 찾을 수 없습니다.")
        );

        userRepository.findById(ownerId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.")
        );

        Menu menu = new Menu(request.getName(), request.getPrice(), request.getDetail(), store);
        Menu savedMenu = menuRepository.save(menu);

        if (savedMenu == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "메뉴 저장에 실패했습니다.");
        }

        return new MenuSaveResponse(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice(),
                                    savedMenu.getDetail(), savedMenu.getCreatedAt(), savedMenu.getUpdatedAt());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> findAll(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 가게를 찾을 수 없습니다.")
        );

        // 삭제되지 않은 메뉴만 조회
        List<Menu> menus = menuRepository.findAllByStoreAndIsDeleted(store, 0);
        return menus.stream()
                .map(menu -> new MenuResponse(
                        menu.getId(), menu.getName(),
                        menu.getPrice(), menu.getDetail(),
                        menu.getCreatedAt(), menu.getUpdatedAt()))
                .toList();
    }

    @Transactional(readOnly = true)
    public MenuResponse findOne(Long storeId, Long menuId) {
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 가게를 찾을 수 없습니다.")
        );

        Menu menu = menuRepository.findByIdAndStoreAndIsDeleted(menuId, store, 0).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 메뉴를 찾을 수 없습니다.")
        );

        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(),
                                menu.getDetail(), menu.getCreatedAt(), menu.getUpdatedAt());
    }

    @Transactional
    public MenuUpdateResponse update(Long ownerId, Long storeId, Long menuId, MenuUpdateRequest request) {
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 가게를 찾을 수 없습니다.")
        );

        Menu menu = menuRepository.findByIdAndStoreAndIsDeleted(menuId, store, 0).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 메뉴를 찾을 수 없습니다.")
        );

        menu.update(request.getName(), request.getPrice(), request.getDetail());

        return new MenuUpdateResponse(menu.getId(), menu.getName(), menu.getPrice(),
                                      menu.getDetail(), menu.getCreatedAt(), menu.getUpdatedAt());
    }

    @Transactional
    public void delete(Long ownerId, Long storeId, Long menuId) {
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 가게를 찾을 수 없습니다.")
        );

        Menu menu = menuRepository.findByIdAndStoreAndIsDeleted(menuId, store, 0).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 메뉴를 찾을 수 없습니다.")
        );

        menu.markAsDeleted(); // 소프트 삭제 처리
    }

}
