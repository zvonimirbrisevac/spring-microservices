package com.microservicestutorial.inventoryservice.repos;

import com.microservicestutorial.inventoryservice.models.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findBySkuCodeIn(List<String> skuCode);

    Optional<Object> findBySkuCode(String skuCode);
}
