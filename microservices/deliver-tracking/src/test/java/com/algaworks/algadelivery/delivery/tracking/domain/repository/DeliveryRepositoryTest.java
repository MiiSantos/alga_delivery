package com.algaworks.algadelivery.delivery.tracking.domain.repository;

import com.algaworks.algadelivery.delivery.tracking.domain.model.ContactPoint;
import com.algaworks.algadelivery.delivery.tracking.domain.model.Delivery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeliveryRepositoryTest {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Test
    public void shouldPersist() {
        Delivery delivery = Delivery.draft();

        delivery.editPreparationDetails(createValidDetails());

        delivery.addItem("Macarrão", 1);
        delivery.addItem("Molho de Tomate", 2);
        delivery.addItem("Salsicha Pacote", 1);

        deliveryRepository.saveAndFlush(delivery);

        Delivery persistedDelivery = deliveryRepository.findById(delivery.getId()).orElseThrow();

        assertEquals(3, delivery.getItems().size());
        assertEquals("Macarrão", persistedDelivery.getItems().getFirst().getName());
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