package com.justbelieveinmyself.marta.controllers;

import com.justbelieveinmyself.marta.domain.annotations.CurrentUser;
import com.justbelieveinmyself.marta.domain.dto.OrderDto;
import com.justbelieveinmyself.marta.domain.dto.ProductDto;
import com.justbelieveinmyself.marta.domain.entities.Order;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.enums.DeliveryStatus;
import com.justbelieveinmyself.marta.exceptions.ResponseError;
import com.justbelieveinmyself.marta.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/orders")
@Tag(name = "Order", description = "The Order API")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @Operation(summary = "Get orders of current user", description = "Returns orders of current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductDto.class))),
    })
    public ResponseEntity<List<OrderDto>> getListOrders(@CurrentUser User user) {
        return orderService.getListOrders(user);
    }
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(
            @CurrentUser User user,
            @RequestBody OrderDto orderDto
    ) {
        return orderService.createOrder(user, orderDto);
    }

    @PutMapping("{orderId}/status")
    @Operation(summary = "Update order-status", description = "Update status value for order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated and saved",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = OrderDto.class))),
            @ApiResponse(responseCode = "403", description = "You don't have the rights!",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ResponseError.class)))
    })
    @Parameter(name = "orderId", schema = @Schema(name = "orderId", type = "integer"), in = ParameterIn.PATH)
    public ResponseEntity<OrderDto> updateOrderStatus(
            @Parameter(hidden = true) @PathVariable("orderId") Order order,
            @RequestBody String status,
            @CurrentUser User authedUser
    ){
        return orderService.changeOrderStatus(order, DeliveryStatus.valueOf(status), authedUser);
    }

}
