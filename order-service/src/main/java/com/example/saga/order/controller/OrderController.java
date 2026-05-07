package com.example.saga.order.controller;

import com.example.saga.order.domain.OrderEntity;
import com.example.saga.order.service.OrderService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Validated
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderEntity create(@RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request.productId(), request.quantity());
    }

    @PatchMapping("/{id}/force-fail-after-payment")
    public OrderEntity enableCompensationDemo(@PathVariable("id") String id) {
        return orderService.enableCompensationDemo(id);
    }

    @GetMapping("/{id}")
    public OrderEntity get(@PathVariable("id") String id) {
        return orderService.get(id);
    }

    @GetMapping
    public List<OrderEntity> list() {
        return orderService.list();
    }

    public record CreateOrderRequest(
            @NotBlank String productId,
            @Min(1) int quantity
    ) {}
}
