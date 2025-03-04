package com.ateen.delivery.domain.menu.service;

import com.ateen.delivery.domain.menu.dto.request.MenuSaveRequest;
import com.ateen.delivery.domain.menu.dto.request.MenuUpdateRequest;
import com.ateen.delivery.domain.menu.dto.response.MenuResponse;
import com.ateen.delivery.domain.menu.dto.response.MenuSaveResponse;
import com.ateen.delivery.domain.menu.dto.response.MenuUpdateResponse;
import com.ateen.delivery.domain.menu.entity.Menu;
import com.ateen.delivery.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public MenuSaveResponse save(Long ownerId, Long storeId, MenuSaveRequest request) {
        //1. 가게 먼저 찾기.
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 가게를 찾을 수 없습니다.")
        );

        //store.getOwnerId() == null 인지에 대해서도 확인해야하지 않나?

        //User(사장)의 ID(owner_id)와 맞는지 확인하는 로직. -> 사장만 메뉴 생성 가능.
        if (!store.getOwnerId().equals(ownerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "이 주문을 생성할 권한이 없습니다.");
        }

        Menu menu = new Menu(request.getName(), request.getPrice(), request.getDetail(), store);
        Menu savedMenu = menuRepository.save(menu);

        return new MenuSaveResponse(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice(), savedMenu.getDetail());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> findAll(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 가게를 찾을 수 없습니다.")
        );

        List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(menu -> new MenuResponse(menu.getId(), menu.getName(),
                                                     menu.getPrice(), menu.getDetail())).toList();
    }

    @Transactional(readOnly = true)
    public MenuResponse findOne(Long storeId, Long menuId) {
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 가게를 찾을 수 없습니다.")
        );

        Menu menu = menuRepository.findByIdAndStore(menuId, store).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 메뉴를 찾을 수 없습니다.")
        );

        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getDetail());
    }

    @Transactional
    public MenuUpdateResponse update(Long ownerId, Long storeId, Long menuId, MenuUpdateRequest request) {
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 가게를 찾을 수 없습니다.")
        );

        if (!store.getOwnerId().equals(ownerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 메뉴를 수정할 권한이 없습니다.");
        }

        Menu menu = menuRepository.findByIdAndStore(menuId, store).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 메뉴를 찾을 수 없습니다.")
        );

        menu.update(request.getName(), request.getPrice(), request.getDetail());

        return new MenuUpdateResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getDetail());
    }

    @Transactional
    public void delete(Long ownerId, Long storeId, Long menuId) {
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 가게를 찾을 수 없습니다.")
        );

        if (!store.getOwnerId().equals(ownerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 메뉴를 삭제할 권한이 없습니다.");
        }

        Menu menu = menuRepository.findByIdAndStore(menuId, store).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 메뉴를 찾을 수 없습니다.")
        );

        menuRepository.deleteById(menu.getId());
    }
}
