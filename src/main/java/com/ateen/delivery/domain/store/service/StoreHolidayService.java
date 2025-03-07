package com.ateen.delivery.domain.store.service;

import com.ateen.delivery.domain.common.exception.ClientException;
import com.ateen.delivery.domain.store.dto.request.StoreHolidayRequest;
import com.ateen.delivery.domain.store.dto.response.StoreHolidayResponse;
import com.ateen.delivery.domain.store.entity.Store;
import com.ateen.delivery.domain.store.entity.holiday.StoreHoliday;
import com.ateen.delivery.domain.store.repository.StoreHolidayRepository;
import com.ateen.delivery.domain.store.repository.StoreRepository;
import com.ateen.delivery.global.dto.Response;
import com.ateen.delivery.global.dto.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StoreHolidayService {

    private final StoreRepository storeRepository;
    private final StoreHolidayRepository storeHolidayRepository;

    @Transactional
    public Response<StoreHolidayResponse> addHoliday(Long userId, Long storeId, StoreHolidayRequest request) {

        Store store = getStoreWithPermission(userId, storeId);

        storeHolidayRepository.findByStoreIdAndSpecialHolidayDate(storeId, request.getSpecialHolidayDate())
                .ifPresent(h -> {
                    throw new ClientException(ErrorCode.DUPLICATE_HOLIDAY, request.getSpecialHolidayDate());
                });

        StoreHoliday storeHoliday = StoreHoliday.createSpecialHoliday(store, request.getSpecialHolidayDate(), request.getHolidayMessage());

        storeHolidayRepository.save(storeHoliday);
        return Response.of(StoreHolidayResponse.from(storeHoliday));
    }

    @Transactional
    public void removeHoliday(Long userId, Long storeId, Long holidayId) {
        Store store = getStoreWithPermission(userId, storeId);

        StoreHoliday storeHoliday = storeHolidayRepository.findByIdAndStoreId(holidayId, storeId)
                .orElseThrow(() -> new ClientException(ErrorCode.HOLIDAY_NOT_FOUND));

        storeHolidayRepository.delete(storeHoliday);
    }

    private Store getStoreWithPermission(Long userId, Long storeId) {
        Store store = storeRepository.findByIdAndIsDeletedFalse(storeId)
                .orElseThrow(() -> new ClientException(ErrorCode.STORE_NOT_FOUND));

        if (!Objects.equals(store.getOwner().getId(), userId)) {
            throw new ClientException(ErrorCode.FORBIDDEN_MODI_REQUEST);
        }

        return store;
    }
}
