package com.algaworks.algadelivery.courier.management.api.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourierRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String phone;
}
