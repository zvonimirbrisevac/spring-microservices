package com.microservicestutorial.orderservice.services;

import com.microservicestutorial.orderservice.DTOs.InventoryResponse;
import com.microservicestutorial.orderservice.DTOs.OrderLineItemDTO;
import com.microservicestutorial.orderservice.events.OrderPlacedEvent;
import com.microservicestutorial.orderservice.models.OrderLineItem;
import com.microservicestutorial.orderservice.DTOs.OrderRequest;
import com.microservicestutorial.orderservice.models.Order;
import com.microservicestutorial.orderservice.repos.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final Tracer tracer;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order(UUID.randomUUID().toString());

        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItemDTOList()
                .stream()
                .map(this::mapToOrderLineItem)
                .toList();


        List<String> skuCodes = orderLineItems.stream()
                .map(OrderLineItem::getSkuCode)
                .toList();

        Span inventoryServiceLookup = tracer.nextSpan().name("InventoryServiceLookup");

        Tracer.SpanInScope isLookup = tracer.withSpan(inventoryServiceLookup.start());

        inventoryServiceLookup.tag("call", "inventory-service");

        // Call Inventory Service, and place order if product is in
        // stock
        InventoryResponse[] inventoryResponsArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsInStock = Arrays.stream(inventoryResponsArray)
                .allMatch(InventoryResponse::isInStock);

        isLookup.close();

        if(allProductsInStock){
            Order order1 = orderRepository.save(order);
            orderLineItems.stream().forEach(orderLineItem -> orderLineItem.setOrder(order1));
            kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order1.getOrderNumber()));
            return "Order Placed Successfully";
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }


    }

    private OrderLineItem mapToOrderLineItem(OrderLineItemDTO orderLineItemsDto) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setPrice(orderLineItemsDto.getPrice());
        orderLineItem.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItem.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItem;
    }
}
