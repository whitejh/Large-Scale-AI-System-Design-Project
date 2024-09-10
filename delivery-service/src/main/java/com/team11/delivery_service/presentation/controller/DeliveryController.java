package com.team11.delivery_service.presentation.controller;

import com.team11.delivery_service.application.service.DeliveryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Order", description="Order API")
@RestController
@RequestMapping("/deliveries")
@Slf4j
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
}
