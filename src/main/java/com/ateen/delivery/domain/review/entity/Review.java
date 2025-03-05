package com.ateen.delivery.domain.review.entity;

import com.ateen.delivery.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "review")
public class Review extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer stars;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public Review(Integer stars, String content, Order order, Store store) {
        this.stars = stars;
        this.content = content;
        this.order = order;
        this.store = store;
    }

    public void update(Integer stars, String content) {
        this.stars = stars;
        this.content = content;
    }
}
