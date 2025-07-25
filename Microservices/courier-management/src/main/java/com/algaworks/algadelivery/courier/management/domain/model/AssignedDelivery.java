package com.algaworks.algadelivery.courier.management.domain.model;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AssignedDelivery {

    @EqualsAndHashCode.Include
    private UUID id;

    private OffsetDateTime assignedAt;

     public static AssignedDelivery pending(UUID deliveryId) {
        AssignedDelivery assignedDelivery = new AssignedDelivery();
        assignedDelivery.setId(deliveryId);
        assignedDelivery.setAssignedAt(OffsetDateTime.now());

        return assignedDelivery;
    }

}