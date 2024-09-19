package com.team11.deliveryDriver_service.application.dto;

import com.team11.deliveryDriver_service.domain.model.Driver;
import com.team11.deliveryDriver_service.domain.model.DriverTypeEnum;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriverRespDto {
    private Long userId;
    private UUID slackId;
    private DriverTypeEnum type;
    private UUID hubId;

    public static DriverRespDto from(Driver driver) {
        return DriverRespDto.builder()
                .userId(driver.getUserId())
                .slackId(driver.getSlackId())
                .type(driver.getType())
                .hubId(driver.getHubId())
                .build();
    }
}
