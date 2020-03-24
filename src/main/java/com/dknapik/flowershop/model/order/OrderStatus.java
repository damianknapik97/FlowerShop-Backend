package com.dknapik.flowershop.model.order;

public enum OrderStatus {
    CREATED,
    WAITING_FOR_ASSIGNMENT,
    ASSIGNED,
    READY_FOR_DELIVERY,
    IN_DELIVERY,
    DONE
}
