package com.ateen.delivery.domain.review.entity;

import com.ateen.delivery.domain.common.entity.BaseEntity;
import com.ateen.delivery.domain.orders.entity.Order;
import com.ateen.delivery.domain.store.entity.Store;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "review")
public class Review extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer stars;

    @Column(nullable = false, length = 1000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public Review(Integer stars, String content, Store store) {
        this.stars = stars;
        this.content = content;
        this.store = store;
    }

    public void update(Integer stars, String content) {
        this.stars = stars;
        this.content = content;
    }
}
