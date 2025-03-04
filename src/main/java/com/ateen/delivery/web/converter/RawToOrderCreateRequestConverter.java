package com.ateen.delivery.web.converter;

import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.orders.constants.OrderType;
import com.ateen.delivery.domain.orders.dto.request.OrderCreateRequest;
import com.ateen.delivery.domain.orders.dto.request.RawCreateRequest;
import org.springframework.core.convert.converter.Converter;

public class RawToOrderCreateRequestConverter implements Converter<RawCreateRequest, OrderCreateRequest> {

    @Override public OrderCreateRequest convert(RawCreateRequest source) {
        return OrderCreateRequest.builder()
                .amount(source.getAmount())
                .orderType(OrderType.valueOf(source.getOrderType()))
                .targetAddress(
                        new Address(source.getCity(), source.getDistrict(), source.getStreet(), source.getDetail())
                )
                .build();
    }
}
