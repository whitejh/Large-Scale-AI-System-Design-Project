package com.team11.deliveryDriver_service.application.service;

import com.team11.deliveryDriver_service.application.dto.DriverRespDto;
import com.team11.deliveryDriver_service.domain.model.Driver;
import com.team11.deliveryDriver_service.domain.repository.DriverRepository;
import com.team11.deliveryDriver_service.infrastructure.feign.HubFeignClient;
import com.team11.deliveryDriver_service.presentation.request.DriverReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;

    private HubFeignClient hubFeignClient;

    // 배송 담당자 추가
    @Transactional
    public DriverRespDto createDriver(DriverReqDto reqDto) {
        String type = reqDto.getType().getDriver();
        UUID hubId = reqDto.getHubId();

        // 배송 담당자 구분
        if(hubId != null && type.equals("업체 배송 담당자")){
            if(!hubFeignClient.isHubIdExist(hubId)) {
                throw new IllegalArgumentException("이 업체 배송 담당자의 소속 허브 ID를 찾을 수 없습니다.");
            }
        }

        Driver driver = DriverReqDto.toDriver(reqDto);

        return DriverRespDto.from(driverRepository.save(driver));
    }

    // 배송 담당자 수정
    @Transactional
    public DriverRespDto updateDriver(DriverReqDto reqDto, Long userId) {
        Driver driver = driverRepository.findByUserIdAndDeletedIsFalse(userId).orElseThrow(
                ()-> new IllegalArgumentException("수정하려는 배송 담당자가 존재하지 않습니다.")
        );

        String type = reqDto.getType().getDriver();

        if(type.equals("허브 배송 담당자")){
            driver.setType(reqDto.getType());
            driver.setHubId(null);
        }else if(type.equals("업체 배송 담당자")){
            if(reqDto.getHubId()==null){
                throw new IllegalArgumentException("소속 허브 ID가 입력되지 않았습니다.");
            }else {
                if(!hubFeignClient.isHubIdExist(reqDto.getHubId())){
                    throw new IllegalArgumentException("수정하려는 업체의 소속 허브 ID를 찾을 수 없습니다.");
                }
                driver.setType(reqDto.getType());
                driver.setHubId(reqDto.getHubId());
            }
        }

        return DriverRespDto.from(driverRepository.save(driver));
    }

    // 배송 담당자 삭제
    @Transactional
    public DriverRespDto deleteDriver(Long userId, String userName) {
        Driver driver = driverRepository.findByUserIdAndDeletedIsFalse(userId).orElseThrow(
                ()-> new IllegalArgumentException("삭제하려는 배송 담당자가 존재하지 않습니다.")
        );

        driver.setDeleted(LocalDateTime.now(), userName);
        return DriverRespDto.from(driverRepository.save(driver));
    }

    // 배송 담당자 조회(본인)
    public DriverRespDto getDriver(Long userId) {
        Driver driver = driverRepository.findByUserIdAndDeletedIsFalse(userId).orElseThrow(
                ()-> new IllegalArgumentException("해당 배송 담당자의 정보를 찾을 수 없습니다.")
        );

        return DriverRespDto.from(driver);
    }

    // 배송 담당자 조회(허브)
    public List<DriverRespDto> getAllDrivers(UUID hubId) {
        List<Driver> driverList = driverRepository.findAllByHubIdAndDeletedIsFalse(hubId).orElseThrow(
                ()-> new IllegalArgumentException("해당 허브에 존재하는 배송 담당자가 없습니다.")
        );

        return driverList.stream().map(DriverRespDto::from).collect(Collectors.toList());
    }


    // FeignClient

    // 슬랙 ID 갱신

    @Transactional
    public void updateSlackId(Long userId, UUID slackId){
        Driver driver = driverRepository.findByUserIdAndDeletedIsFalse(userId).orElseThrow(
                ()-> new IllegalArgumentException("배송 담당자의 슬랙 ID를 갱신하는데 실패했습니다.")
        );

        driver.setSlackId(slackId);
        driverRepository.save(driver);
    }
}
