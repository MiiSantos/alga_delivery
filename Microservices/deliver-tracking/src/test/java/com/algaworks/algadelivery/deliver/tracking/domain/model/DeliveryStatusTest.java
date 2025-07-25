package com.algaworks.algadelivery.deliver.tracking.domain.model;

import com.algaworks.algadelivery.delivery.tracking.domain.exception.model.DeliveryStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryStatusTest {

    @Test
    public void canChangeToWaintingCourier() {
        assertTrue(DeliveryStatus.CREATED.canChangeTo(DeliveryStatus.WAITING_COURIER));
    }

    @Test
    public void canNotChangeToInTransit() {
        assertFalse(DeliveryStatus.CREATED.canChangeTo(DeliveryStatus.IN_TRANSIT));
    }
}