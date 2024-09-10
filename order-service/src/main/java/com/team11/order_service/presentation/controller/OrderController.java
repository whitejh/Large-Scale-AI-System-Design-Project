package com.team11.order_service.presentation.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Product", description="Product API")
@RestController
@RequestMapping("/orders")
@Slf4j
@RequiredArgsConstructor
public class OrderController {
}
