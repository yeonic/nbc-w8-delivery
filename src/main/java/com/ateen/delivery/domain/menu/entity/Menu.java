package com.ateen.delivery.domain.menu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Menu {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer price;

    private String detail;

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
}
