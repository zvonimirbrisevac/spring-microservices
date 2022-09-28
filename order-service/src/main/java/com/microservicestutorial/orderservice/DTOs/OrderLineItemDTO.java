package com.microservicestutorial.orderservice.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class OrderLineItemDTO {
//    private Long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
}
