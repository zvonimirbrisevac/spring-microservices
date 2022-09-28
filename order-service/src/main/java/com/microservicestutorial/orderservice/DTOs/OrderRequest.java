package com.microservicestutorial.orderservice.DTOs;

import com.microservicestutorial.orderservice.models.OrderLineItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private List<OrderLineItemDTO> orderLineItemDTOList;
}
