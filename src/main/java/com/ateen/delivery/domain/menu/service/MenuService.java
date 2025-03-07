package com.ateen.delivery.domain.menu.service;

import com.ateen.delivery.domain.common.exception.ClientException;
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
import com.ateen.delivery.global.dto.error.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public MenuSaveResponse save(Long ownerId, Long storeId, MenuSaveRequest request) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ClientException(ErrorCode.STORE_NOT_FOUND));

        userRepository.findById(ownerId)
                .orElseThrow(() -> new ClientException(ErrorCode.USER_NOT_FOUND));

        Menu menu = new Menu(request.getName(), request.getPrice(), request.getDetail(), store);
        Menu savedMenu = menuRepository.save(menu);

        return new MenuSaveResponse(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice(), savedMenu.getDetail());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> findAll(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ClientException(ErrorCode.STORE_NOT_FOUND));

        // 삭제되지 않은 메뉴만 조회
        List<Menu> menus = menuRepository.findAllByStoreAndIsDeleted(store, 0);

        if (menus == null || menus.isEmpty()) {
            return List.of();
        }

        return menus.stream()
                .map(menu -> new MenuResponse(
                        menu.getId(), menu.getName(),
                        menu.getPrice(), menu.getDetail()))
                .toList();
    }

    @Transactional(readOnly = true)
    public MenuResponse findOne(Long storeId, Long menuId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ClientException(ErrorCode.STORE_NOT_FOUND));

        Menu menu = menuRepository.findByIdAndStoreAndIsDeleted(menuId, store, 0)
                .orElseThrow(() -> new ClientException(ErrorCode.MENU_NOT_FOUND));

        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getDetail());
    }

    @Transactional
    public MenuUpdateResponse update(Long storeId, Long menuId, MenuUpdateRequest request) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ClientException(ErrorCode.STORE_NOT_FOUND));

        Menu menu = menuRepository.findByIdAndStoreAndIsDeleted(menuId, store, 0)
                .orElseThrow(() -> new ClientException(ErrorCode.MENU_NOT_FOUND));

        menu.update(request.getName(), request.getPrice(), request.getDetail());

        return new MenuUpdateResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getDetail());
    }

    @Transactional
    public void delete(Long storeId, Long menuId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ClientException(ErrorCode.STORE_NOT_FOUND));

        Menu menu = menuRepository.findByIdAndStoreAndIsDeleted(menuId, store, 0)
                .orElseThrow(() -> new ClientException(ErrorCode.MENU_NOT_FOUND));

        menu.markAsDeleted(); // 소프트 삭제 처리
    }

}
