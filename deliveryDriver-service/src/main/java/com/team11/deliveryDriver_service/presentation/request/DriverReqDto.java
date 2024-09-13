package com.team11.deliveryDriver_service.presentation.request;

import com.team11.deliveryDriver_service.domain.model.Driver;
import com.team11.deliveryDriver_service.domain.model.DriverTypeEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriverReqDto {
    @NotBlank
    private Long userId;
    @NotBlank
    private UUID slackId;

    @NotBlank
    private DriverTypeEnum type;
    private UUID hubId;

    public static Driver toDriver(DriverReqDto reqDto) {
        return Driver.builder()
                .userId(reqDto.getUserId())
                .slackId(reqDto.getSlackId())
                .type(reqDto.getType())
                .hubId(reqDto.getHubId())
                .build();
    }
}
