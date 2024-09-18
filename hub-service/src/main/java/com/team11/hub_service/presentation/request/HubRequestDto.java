package com.team11.hub_service.presentation.request;

import com.team11.hub_service.domain.model.Hub;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HubRequestDto {

    @NotBlank
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;

    public static Hub toHub(HubRequestDto reqDto) {
        return Hub.builder()
                .name(reqDto.getName())
                .address(reqDto.getAddress())
                .latitude(reqDto.getLatitude())
                .longitude(reqDto.getLongitude())
                .build();
    }


}
