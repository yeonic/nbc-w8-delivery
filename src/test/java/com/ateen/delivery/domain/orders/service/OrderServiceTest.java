package com.ateen.delivery.domain.orders.service;

import com.ateen.delivery.domain.orders.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock OrderRepository repository;
    @InjectMocks OrderService service;

    @Test
    void 주문이_정상적으로_생성된다() {
        
    }
}