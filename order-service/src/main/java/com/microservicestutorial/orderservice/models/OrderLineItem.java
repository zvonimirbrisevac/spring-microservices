package com.microservicestutorial.orderservice.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_line_item_id_seq")
    @SequenceGenerator(name = "order_line_item_id_seq", sequenceName = "order_line_item_id_seq", allocationSize = 1)
    private Long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
    @ManyToOne
    private Order order;

    public OrderLineItem(String skuCode, BigDecimal price, Integer quantity, Order order) {
        this.skuCode = skuCode;
        this.price = price;
        this.quantity = quantity;
        this.order = order;
    }
}
