package com.ateen.delivery.domain.review.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer stars;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders orders;

    public Review(Integer stars, String content, Orders orders) {
        this.stars = stars;
        this.content = content;
        this.orders = orders;
    }

    public void update(Integer stars, String content) {
        this.stars = stars;
        this.content = content;
    }
}
