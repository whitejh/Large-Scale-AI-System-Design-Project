package com.team11.delivery_service.application.service;

import com.team11.delivery_service.application.dto.DeliveryRespDto;
import com.team11.delivery_service.domain.model.Delivery;
import com.team11.delivery_service.domain.model.DeliveryStatusEnum;
import com.team11.delivery_service.domain.repository.DeliveryPathRepository;
import com.team11.delivery_service.domain.repository.DeliveryRepository;
import com.team11.delivery_service.infrastructure.feign.CompanyFeignClient;
import com.team11.delivery_service.presentation.request.RecipientReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryPathRepository deliveryPathRepository;

    private CompanyFeignClient companyFeignClient;

    // 배송 생성
    @Transactional
    public UUID createDelivery(UUID supplyCompanyId, UUID receiveCompanyId, String recipientName, UUID recipientSlackId){
        // 공급 업체ID, 수령 업체ID로 각각 소속된 허브 ID 받아오기
        UUID originHubId = companyFeignClient.getHubIdByCompanyId(supplyCompanyId);
        UUID destinationHubId = companyFeignClient.getHubIdByCompanyId(receiveCompanyId);

        // 수령 업체 ID로 배송지 주소 받아오기
        String companyAddy = companyFeignClient.getCompanyAddy(receiveCompanyId);

        // 배송 담당자 배정
        // 허브 배송 담당자 10명 중 무작위로 한 명을 고르는 알고리즘
        // 업체 배송 담당자 중 도착 허브 ID에 속한 업체 배송 담당자 10명 중 무작위로 한 명을 고르는 알고리즘

        // 배송 생성 + 배송 담당자 추가해야함
        Delivery delivery = new Delivery(originHubId, destinationHubId, companyAddy, DeliveryStatusEnum.PENDING, recipientName, recipientSlackId);

        // 배송 기록
        // 출발 허브 ID, 도착 허브 ID, 시간, 거리 List 형식으로 받아와서 반복문 돌아 배송 기록 생성



        deliveryRepository.save(delivery);

        return delivery.getDeliveryId();

    }

    // 배송 수정
    @Transactional
    public DeliveryRespDto updateDelivery(RecipientReqDto reqDto, UUID deliveryId){
        Delivery delivery = deliveryRepository.findByDeliveryIdAndDeletedIsFalse(deliveryId).orElseThrow(
                ()-> new IllegalStateException("수정하려는 배송 내역을 찾을 수 없습니다.")
        );

        delivery.updateRecipient(reqDto);

        return DeliveryRespDto.from(deliveryRepository.save(delivery));
    }

    // 배송 삭제
    @Transactional
    public DeliveryRespDto deleteDelivery(UUID deliveryId, String userName){
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(
                ()-> new IllegalArgumentException("삭제하려는 배송 내역을 찾을 수 없습니다.")
        );

        delivery.setDeleted(LocalDateTime.now(), userName);

        return DeliveryRespDto.from(deliveryRepository.save(delivery));
    }

    // 배송 조회 (허브 별)
    public List<DeliveryRespDto> getDeliveryByHub(UUID hubId,Pageable pageable){
        Page<Delivery> deliveryList = deliveryRepository.findAllByHubIdAndDeletedIsFalseOrderByCreatedAtDesc(hubId, pageable).orElseThrow(
                ()-> new IllegalArgumentException("해당 허브에 배송 내역이 존재하지 않습니다.")
        );

        return deliveryList.stream().map(DeliveryRespDto::from).collect(Collectors.toList());
    }

    // 배송 조회 (업체 별)
    public List<DeliveryRespDto> getDeliveryByCompany(UUID companyId, Pageable pageable){
        String companyAddy = companyFeignClient.getCompanyAddy(companyId);

        Page<Delivery> deliveryList = deliveryRepository.findAllByDeliveryAddressAndDeletedIsFalseOrderByCreatedAtDesc(companyAddy, pageable).orElseThrow(
                ()-> new IllegalArgumentException("해당 업체의 배송 내역이 존재하지 않습니다.")
        );

        return deliveryList.stream().map(DeliveryRespDto::from).collect(Collectors.toList());
    }
}
