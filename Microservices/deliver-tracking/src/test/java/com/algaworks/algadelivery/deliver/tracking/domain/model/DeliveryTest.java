package com.algaworks.algadelivery.deliver.tracking.domain.model;

import com.algaworks.algadelivery.delivery.tracking.domain.exception.DomainException;
import com.algaworks.algadelivery.delivery.tracking.domain.exception.model.ContactPoint;
import com.algaworks.algadelivery.delivery.tracking.domain.exception.model.Delivery;
import com.algaworks.algadelivery.delivery.tracking.domain.exception.model.DeliveryStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryTest {

    @Test
    public void shouldChangeToPlaced() {
        Delivery delivery = Delivery.draft();

        delivery.editPreparationDetails(createValidDetails());

        delivery.place();

        assertEquals(DeliveryStatus.WAITING_COURIER, delivery.getStatus());
        assertNotNull(delivery.getPlacedAt());
    }

    @Test
    public void shouldNotChangeToPlaced() {
        Delivery delivery = Delivery.draft();

        assertThrows(DomainException.class, delivery::place);

        assertEquals(DeliveryStatus.CREATED, delivery.getStatus());
        assertNull(delivery.getPlacedAt());
    }

    private Delivery.PreparationDetails createValidDetails() {
        ContactPoint sender = ContactPoint.builder()
                .zipCode("04010-068")
                .street("Rua Mirandinha")
                .number("130")
                .complement("")
                .name("Nininho da Silva")
                .phone("(00)0000-0000")
                .build();

        ContactPoint receiver = ContactPoint.builder()
                .zipCode("04010-068")
                .street("Av São João")
                .number("1500")
                .complement("Sala 107")
                .name("Arthur Bittencourt")
                .phone("(00)0000-0000")
                .build();

        return Delivery.PreparationDetails.builder()
                .sender(sender)
                .receiver(receiver)
                .distanceFee(new BigDecimal("15.00"))
                .courierPayout(new BigDecimal("8.50"))
                .expectedDeliveryTime(Duration.ofHours(5))
                .build();
    }

}