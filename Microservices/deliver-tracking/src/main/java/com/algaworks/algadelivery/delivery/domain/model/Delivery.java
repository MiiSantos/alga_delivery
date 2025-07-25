package com.algaworks.algadelivery.deliver.tracking.domain.model;

import com.algaworks.algadelivery.deliver.tracking.domain.exception.DomainException;
import lombok.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter(AccessLevel.PRIVATE)
@Getter
public class Delivery {

    @EqualsAndHashCode.Include
    private UUID id;

    private UUID courierId;

    private OffsetDateTime placedAt;
    private OffsetDateTime assignedAt;
    private OffsetDateTime expectedDeliveryAt;
    private OffsetDateTime fulfilledAt;

    private BigDecimal distanceFee;
    private BigDecimal courierPayout;
    private BigDecimal totalCost;

    private Integer totalItems;
    private DeliveryStatus status;
    private List<Item> items = new ArrayList<>();
    private ContactPoint sender;
    private ContactPoint receiver;

    public static Delivery draft() {
        Delivery delivery = new Delivery();
        delivery.setId(UUID.randomUUID());
        delivery.setStatus(DeliveryStatus.CREATED);
        delivery.setTotalItems(0);
        delivery.setTotalCost(BigDecimal.ZERO);
        delivery.setCourierPayout(BigDecimal.ZERO);
        delivery.setDistanceFee(BigDecimal.ZERO);

        return delivery;
    }

    public UUID addItem(String name, Integer quantity) {
        Item item = Item.brandNew(name, quantity);
        items.add(item);
        calculateItems();
        return item.getId();
    }

    public void removeItem(UUID itemId) {
        items.removeIf(item -> item.getId().equals(itemId));
        calculateItems();
    }

    public void changeItemQuantity(UUID itemId, Integer quantity) {
        Item item = items.stream().filter(i -> i.getId().equals(itemId))
                .findFirst().orElseThrow();

        item.setQuantity(quantity);
        calculateItems();
    }

    public void removeItems() {
        items.clear();
        calculateItems();
    }

    public void editPreparationDetails(PreparationDetails details) {
        verifyIfCanBeEdited();
        setSender(details.getSender());
        setReceiver(details.getReceiver());
        setDistanceFee(details.getDistanceFee());
        setCourierPayout(details.getCourierPayout());

        setExpectedDeliveryAt(OffsetDateTime.now().plus(details.getExpectedDeliveryTime()));
        setTotalCost(getDistanceFee().add(getCourierPayout()));
    }

    public void place() {
        verifyIfCanBePlaced();
        changeStatusTo(DeliveryStatus.WAITING_COURIER);
        setPlacedAt(OffsetDateTime.now());
    }

    public void pickUp(UUID courierId) {
        setCourierId(courierId);
        changeStatusTo(DeliveryStatus.IN_TRANSIT);
        setAssignedAt(OffsetDateTime.now());
    }

    public void markAsDelivered() {
        changeStatusTo(DeliveryStatus.DELIVERED);
        setFulfilledAt(OffsetDateTime.now());
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(this.items);
    }

    private void calculateItems() {
        int total = items.stream().mapToInt(Item::getQuantity).sum();
        setTotalItems(total);
    }

    private void verifyIfCanBePlaced() {
        if (!isFilled()) {
            throw new DomainException("Check if the delivery has sender, receiver and cost.");
        }
        if (!getStatus().equals(DeliveryStatus.CREATED)) {
            throw new DomainException("Check if the delivery has already picked up.");
        }
    }

    private void verifyIfCanBeEdited() {
        if (!getStatus().equals(DeliveryStatus.CREATED)) {
            throw new DomainException("Check if the delivery already exists.");
        }
    }

    private boolean isFilled() {
        return getSender() != null
                && getReceiver() != null
                && getTotalCost() != null;
    }
    private void changeStatusTo(DeliveryStatus newStatus) {
        if (newStatus != null && !getStatus().canChangeTo(newStatus)) {
            throw new DomainException("Invalid status " + getStatus() + "to " + newStatus);
        }
        setStatus(newStatus);
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PreparationDetails {
        private ContactPoint sender;
        private ContactPoint receiver;
        private BigDecimal distanceFee;
        private BigDecimal courierPayout;
        private Duration expectedDeliveryTime;
    }
}
