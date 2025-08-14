package com.algaworks.algadelivery.delivery.tracking.domain.service;

import com.algaworks.algadelivery.delivery.tracking.api.model.ContactPointRequest;
import com.algaworks.algadelivery.delivery.tracking.api.model.DeliveryRequest;
import com.algaworks.algadelivery.delivery.tracking.api.model.ItemRequest;
import com.algaworks.algadelivery.delivery.tracking.domain.exception.DomainException;
import com.algaworks.algadelivery.delivery.tracking.domain.model.ContactPoint;
import com.algaworks.algadelivery.delivery.tracking.domain.model.Delivery;
import com.algaworks.algadelivery.delivery.tracking.domain.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryPreparationService {

    private final DeliveryRepository deliveryRepository;

    @Transactional
    public Delivery draft(DeliveryRequest request) {
        Delivery delivery = Delivery.draft();
        handlePreparation(request, delivery);
        return deliveryRepository.saveAndFlush(delivery);
    }

    @Transactional
    public Delivery edit(UUID deliveryId, DeliveryRequest request) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DomainException("Delivery not found!"));
        delivery.removeItems();
        handlePreparation(request, delivery);
        return deliveryRepository.saveAndFlush(delivery);
    }

    private void handlePreparation(DeliveryRequest request, Delivery delivery) {
        ContactPointRequest senderRequest = request.getSender();
        ContactPointRequest receiverRequest = request.getReceiver();

        ContactPoint sender = new ContactPoint().builder()
                .zipCode(senderRequest.getZipCode())
                .phone(senderRequest.getPhone())
                .complement(senderRequest.getComplement())
                .number(senderRequest.getNumber())
                .name(senderRequest.getName())
                .street(senderRequest.getStreet())
                .build();
        ContactPoint receiver = new ContactPoint().builder()
                .zipCode(receiverRequest.getZipCode())
                .phone(receiverRequest.getPhone())
                .complement(receiverRequest.getComplement())
                .name(receiverRequest.getName())
                .number(receiverRequest.getNumber())
                .street(receiverRequest.getStreet())
                .build();

        Duration expectedDeliveryTime = Duration.ofHours(3);
        BigDecimal payout = new BigDecimal("58,00");
        BigDecimal distanceFee = new BigDecimal("12");

        var preparationDetails = Delivery.PreparationDetails.builder()
                .receiver(receiver)
                .sender(sender)
                .expectedDeliveryTime(expectedDeliveryTime)
                .courierPayout(payout)
                .distanceFee(distanceFee)
                .build();

        delivery.editPreparationDetails(preparationDetails);

        for (ItemRequest item : request.getItems()) {
            delivery.addItem(item.getName(), item.getQuantity());
        }
    }
}
