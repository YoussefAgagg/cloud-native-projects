package com.github.youssefagagg.orderservice.event;

public record OrderDispatchedMessage(
        Long orderId
) {
}