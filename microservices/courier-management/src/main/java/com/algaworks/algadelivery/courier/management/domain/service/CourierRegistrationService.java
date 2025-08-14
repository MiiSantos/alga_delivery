package com.algaworks.algadelivery.courier.management.domain.service;

import com.algaworks.algadelivery.courier.management.api.model.CourierRequest;
import com.algaworks.algadelivery.courier.management.domain.exception.DomainException;
import com.algaworks.algadelivery.courier.management.domain.model.Courier;
import com.algaworks.algadelivery.courier.management.domain.repository.CourierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CourierRegistrationService {

    private final CourierRepository courierRepository;

    public Courier create(CourierRequest request) {
        Courier courier = Courier.brandNew(request.getName(), request.getPhone());
        return courierRepository.saveAndFlush(courier);
    }

    public Courier update(UUID courierId, CourierRequest request) {
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new DomainException("Courier not found!"));
        courier.setName(request.getName());
        courier.setPhone(request.getPhone());
        return courierRepository.saveAndFlush(courier);
    }
}
