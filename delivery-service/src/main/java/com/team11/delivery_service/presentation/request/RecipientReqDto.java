package com.team11.delivery_service.presentation.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipientReqDto {
    @NotBlank
    private String recipientName;
    @NotBlank
    private UUID recipientSlackId;
}
