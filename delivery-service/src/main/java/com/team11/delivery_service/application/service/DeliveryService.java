package com.team11.delivery_service.application.service;

import com.team11.delivery_service.domain.model.Delivery;
import com.team11.delivery_service.domain.model.DeliveryStatusEnum;
import com.team11.delivery_service.domain.repository.DeliveryRepository;
import com.team11.delivery_service.infrastructure.feign.CompanyFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    private CompanyFeignClient companyFeignClient;

    // 배송 생성
    public UUID createDelivery(UUID supplyCompanyId, UUID receiveCompanyId, String recipientName, UUID recipientSlackId){
        // 공급 업체ID, 수령 업체ID로 각각 소속된 허브 ID 받아오기
        UUID originHubId = companyFeignClient.getHubIdByCompanyId(supplyCompanyId);
        UUID destinationHubId = companyFeignClient.getHubIdByCompanyId(receiveCompanyId);

        // 수령 업체 ID로 배송지 주소 받아오기
        String companyAddy = companyFeignClient.getCompanyAddy(receiveCompanyId);

        // 배송 생성
        Delivery delivery = new Delivery(originHubId, destinationHubId, companyAddy, DeliveryStatusEnum.PENDING, recipientName, recipientSlackId);

        deliveryRepository.save(delivery);

        return delivery.getDeliveryId();

    }
}
