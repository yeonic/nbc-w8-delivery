package com.ateen.delivery.domain.menu.entity;

import com.ateen.delivery.domain.common.entity.BaseEntity;
import com.ateen.delivery.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Menu extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(length = 100)
    private String detail;

    @Column(name = "is_deleted", columnDefinition = "TINYINT(1) DEFAULT 0") // 삭제 여부 기본값 0
    private Integer isDeleted = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public Menu(String name, Integer price, String detail, Store store) {
        this.name = name;
        this.price = price;
        this.detail = detail;
        this.store = store;
    }

    public void update(String name, Integer price, String detail) {
        this.name = name;
        this.price = price;
        this.detail = detail;
    }

    public void markAsDeleted() {
        this.isDeleted = 1;
    }
}
