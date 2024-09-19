package com.team11.delivery_service.application.service;

import com.team11.delivery_service.application.dto.PathResultsDto;
import com.team11.delivery_service.domain.model.DeliveryPath;
import com.team11.delivery_service.domain.model.DeliveryStatusEnum;
import com.team11.delivery_service.domain.repository.DeliveryPathRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryPathService {

    private final DeliveryPathRepository deliveryPathRepository;

    public void createDeliveryPath(List<PathResultsDto> dtoList, UUID deliveryId, DeliveryStatusEnum status) {
        for(int i=0; i<dtoList.size(); i++) {
            PathResultsDto dto = dtoList.get(i);
            DeliveryPath deliveryPath = DeliveryPath.from(dto, deliveryId,i++, status);
            deliveryPathRepository.save(deliveryPath);
        }

    }
}
