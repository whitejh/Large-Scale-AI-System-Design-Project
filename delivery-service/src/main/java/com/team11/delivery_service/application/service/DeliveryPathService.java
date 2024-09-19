package com.team11.delivery_service.application.service;

import com.team11.delivery_service.application.dto.PathResultsDto;
import com.team11.delivery_service.domain.model.DeliveryPath;
import com.team11.delivery_service.domain.repository.DeliveryPathRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryPathService {

    private final DeliveryPathRepository deliveryPathRepository;

    public void createDeliveryPath(List<PathResultsDto> dtoList) {

        DeliveryPath deliveryPath = new DeliveryPath();
    }
}
