package com.github.youssefagagg.orderservice.event;

public record OrderAcceptedMessage(
        Long orderId
) {
}