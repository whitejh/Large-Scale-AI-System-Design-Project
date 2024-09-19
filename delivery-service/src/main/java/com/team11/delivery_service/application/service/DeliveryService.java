package com.team11.delivery_service.application.service;

import com.team11.delivery_service.application.dto.DeliveryRespDto;
import com.team11.delivery_service.domain.model.Delivery;
import com.team11.delivery_service.domain.model.DeliveryStatusEnum;
import com.team11.delivery_service.domain.repository.DeliveryPathRepository;
import com.team11.delivery_service.domain.repository.DeliveryRepository;
import com.team11.delivery_service.infrastructure.feign.CompanyFeignClient;
import com.team11.delivery_service.infrastructure.feign.DriverFeignClient;
import com.team11.delivery_service.infrastructure.feign.UserFeignClient;
import com.team11.delivery_service.presentation.request.RecipientReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryPathRepository deliveryPathRepository;

    private UserFeignClient userFeignClient;
    private CompanyFeignClient companyFeignClient;
    private DriverFeignClient driverFeignClient;

    // 배송 생성
    @Transactional
    public UUID createDelivery(UUID supplyCompanyId, UUID receiveCompanyId, String recipientName, UUID recipientSlackId, String userName){
        // 공급 업체ID, 수령 업체ID로 각각 소속된 허브 ID 받아오기
        UUID originHubId = companyFeignClient.getHubIdByCompanyId(supplyCompanyId);
        UUID destinationHubId = companyFeignClient.getHubIdByCompanyId(receiveCompanyId);

        // 수령 업체 ID로 배송지 주소 받아오기
        String companyAddy = companyFeignClient.getCompanyAddy(receiveCompanyId);

        // 배송 담당자 배정
        // 허브 배송 담당자 10명 중 무작위로 한 명을 고르는 알고리즘
        List<Long> hubDrivers = driverFeignClient.getHubDrivers();

        Random random = new Random();
        int randomIndex = random.nextInt(hubDrivers.size());
        Long hubDriverId = hubDrivers.get(randomIndex);

        // 배송 담당자 10명 중 무작위로 한 명을 고르는 알고리즘
        List<Long> companyDrivers = driverFeignClient.getCompanyDrivers(receiveCompanyId);
        Long companyDriverId = companyDrivers.get(randomIndex);

        // 배송 생성
        Delivery delivery = new Delivery(hubDriverId, companyDriverId, originHubId, destinationHubId, companyAddy, DeliveryStatusEnum.PENDING, recipientName, recipientSlackId);

        // 배송 기록
        // 출발 허브 ID, 도착 허브 ID, 시간, 거리 List 형식으로 받아와서 반복문 돌아 배송 기록 생성



        delivery.setCreated(userName);

        deliveryRepository.save(delivery);

        return delivery.getDeliveryId();

    }

    // 배송 수정
    @Transactional
    public DeliveryRespDto updateDelivery(RecipientReqDto reqDto, UUID deliveryId, String userName, String role){
        Delivery delivery = deliveryRepository.findByDeliveryIdAndDeletedIsFalse(deliveryId).orElseThrow(
                ()-> new IllegalStateException("수정하려는 배송 내역을 찾을 수 없습니다.")
        );

        // 권한 확인
        if(role.equals("MANAGER")){
            UUID hubId = userFeignClient.getUserHubId(userName);
            UUID originHubId = delivery.getOriginHubId();
            UUID destinationHubId = delivery.getDestinationHubId();

            if(!originHubId.equals(hubId) || destinationHubId.equals(hubId)){
                throw new IllegalArgumentException("해당 배송 수정에 대해 권한이 없습니다.");
            }
        }else if(role.equals("DRIVER")){
            Long hubDriverId = delivery.getHubDriverId();
            Long companyDriverId = delivery.getCompanyDriverId();
            Long userId = userFeignClient.getUserId(userName);

            if(!(userId.equals(hubDriverId) || userId.equals(companyDriverId))){
                throw new IllegalArgumentException("해당 배송 수정에 대해 권한이 없습니다.");
            }
        }

        delivery.updateRecipient(reqDto);
        delivery.setUpdated(userName);

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
    public List<DeliveryRespDto> getDeliveryByHub(UUID hubId,Pageable pageable, String userName, String role){
        Page<Delivery> deliveryList = deliveryRepository.findAllByHubIdAndDeletedIsFalseOrderByCreatedAtDesc(hubId, pageable).orElseThrow(
                ()-> new IllegalArgumentException("해당 허브에 배송 내역이 존재하지 않습니다.")
        );

        // 권한 확인
        if(role.equals("MANAGER")){
            UUID userHubId = userFeignClient.getUserHubId(userName);

            if(!userHubId.equals(hubId)){
                throw new IllegalArgumentException("해당 배송 조회에 권한이 없습니다.");
            }
        }

        return deliveryList.stream().map(DeliveryRespDto::from).collect(Collectors.toList());
    }

    // 배송 조회 (업체 별)
    public List<DeliveryRespDto> getDeliveryByCompany(UUID companyId, Pageable pageable, String userName, String role){
        String companyAddy = companyFeignClient.getCompanyAddy(companyId);

        Page<Delivery> deliveryList = deliveryRepository.findAllByDeliveryAddressAndDeletedIsFalseOrderByCreatedAtDesc(companyAddy, pageable).orElseThrow(
                ()-> new IllegalArgumentException("해당 업체의 배송 내역이 존재하지 않습니다.")
        );

        // 권한 확인
        if(role.equals("COMPANY")){
            UUID userHubId = userFeignClient.getUserHubId(userName);
            UUID hubId = companyFeignClient.getHubIdByCompanyId(companyId);

            if(!userHubId.equals(hubId)){
                throw new IllegalArgumentException("해당 조회에 권한이 없습니다.");
            }
        }

        return deliveryList.stream().map(DeliveryRespDto::from).collect(Collectors.toList());
    }

    // 허브 배송 담당자 id 반환
    public Long getDriverId(UUID deliveryId){
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(
                ()-> new IllegalArgumentException("해당 배송 내역을 찾을 수 없습니다.")
        );

        Long userId = delivery.getHubDriverId();

        return userId;
    }
}
