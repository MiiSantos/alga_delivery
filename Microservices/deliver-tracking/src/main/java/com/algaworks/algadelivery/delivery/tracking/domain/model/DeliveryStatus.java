package com.algaworks.algadelivery.delivery.tracking.domain.model;

import java.util.Arrays;
import java.util.List;

public enum DeliveryStatus {
    CREATED,
    WAITING_COURIER(CREATED),
    IN_TRANSIT(WAITING_COURIER),
    DELIVERED(IN_TRANSIT);

    private final List<DeliveryStatus> previousStatuses;

    DeliveryStatus(DeliveryStatus... previousStatuses) {
        this.previousStatuses = Arrays.asList(previousStatuses);
    }

    public boolean canChangeTo(DeliveryStatus newStatus) {
        DeliveryStatus currentStatus = this;
        return newStatus.previousStatuses.contains(currentStatus);
    }
}
