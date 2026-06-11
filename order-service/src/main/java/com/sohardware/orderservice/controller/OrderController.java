package com.sohardware.orderservice.controller;

import com.sohardware.orderservice.OrderMapper;
import com.sohardware.orderservice.dto.OrderRequest;
import com.sohardware.orderservice.dto.OrderResponse;
import com.sohardware.orderservice.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    public OrderController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @GetMapping("/all")
    public List<OrderResponse>getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping()
    public Page<OrderResponse> getAllOrders(
            @RequestParam(name = "page", defaultValue="0") int page,
            @RequestParam(name = "size", defaultValue="10") int size
    ) {
        return orderService.getAllOrdersPaginated(page, size);
    }

    // si la validation échoue, Spring  lève une exception MethodArgumentNotValidException
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createOrder(orderRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderResponse);
    }

}
