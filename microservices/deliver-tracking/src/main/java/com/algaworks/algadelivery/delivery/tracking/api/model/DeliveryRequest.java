package com.algaworks.algadelivery.delivery.tracking.api.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeliveryRequest {

    @NotNull
    @Valid
    private ContactPointRequest sender;

    @NotNull
    @Valid
    private ContactPointRequest receiver;

    @NotEmpty
    @Valid
    @Min(1)
    private List<ItemRequest> items;
}
