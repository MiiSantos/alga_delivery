package com.algaworks.algadelivery.courier.management.domain.model;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Courier {

    @EqualsAndHashCode.Include
    private UUID id;

    @Setter(AccessLevel.PUBLIC)
    private String name;

    @Setter(AccessLevel.PUBLIC)
    private String phone;

    private Integer fulfilledDeliveriesQuantity;
    private Integer pendingDeliveiresQuantity;
    private OffsetDateTime lastFulfilledDeiveryAt;
    private List<AssignedDelivery> pendingDeliveries = new ArrayList<>();

    public List<AssignedDelivery> getPendingDeliveries() {
        return Collections.unmodifiableList(this.pendingDeliveries);
    }

    public static Courier brandNew(String name, String phone) {
        Courier courier = new Courier();
        courier.setId(UUID.randomUUID());
        courier.setName(name);
        courier.setPhone(phone);
        courier.setFulfilledDeliveriesQuantity(0);
        courier.setPendingDeliveiresQuantity(0);

        return courier;
    }

    public void assign(UUID deliveryId) {
        pendingDeliveries.add(AssignedDelivery.pending(deliveryId));
        pendingDeliveiresQuantity++;
    }

    private void fulfill(UUID deliveryId) {
        AssignedDelivery delivery = pendingDeliveries.stream()
                .filter(i -> i.getId().equals(deliveryId))
                .findFirst().orElseThrow();
        pendingDeliveries.remove(delivery);
        pendingDeliveiresQuantity--;
        fulfilledDeliveriesQuantity++;
        setLastFulfilledDeiveryAt(OffsetDateTime.now());
    }
}
