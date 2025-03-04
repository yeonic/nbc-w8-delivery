package com.ateen.delivery.web.converter;

import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.orders.constants.OrderType;
import com.ateen.delivery.domain.orders.dto.request.OrderCreateRequest;
import com.ateen.delivery.domain.orders.dto.request.RawCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class ConverterTest {

    @Test
    void 주문_생성_DTO_컨버터가_정상_작동한다() {
        // given
        RawToOrderCreateRequestConverter converter = new RawToOrderCreateRequestConverter();
        RawCreateRequest rawRequest = new RawCreateRequest();
        ReflectionTestUtils.setField(rawRequest, "amount", 1);
        ReflectionTestUtils.setField(rawRequest, "orderType", "DELIVERY");
        ReflectionTestUtils.setField(rawRequest, "city", "city");
        ReflectionTestUtils.setField(rawRequest, "district", "district");
        ReflectionTestUtils.setField(rawRequest, "street", "street");
        ReflectionTestUtils.setField(rawRequest, "detail", "detail");

        // when
        OrderCreateRequest result = converter.convert(rawRequest);

        // then
        assertThat(result.getAmount()).isEqualTo(rawRequest.getAmount());
        assertThat(result.getOrderType()).isEqualTo(OrderType.valueOf(rawRequest.getOrderType()));
        assertThat(result.getTargetAddress()).isEqualTo(new Address(rawRequest.getCity(), rawRequest.getDistrict(),
                rawRequest.getStreet(), rawRequest.getDetail()));
    }
}