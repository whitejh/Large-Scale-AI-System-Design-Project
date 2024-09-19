package com.team11.hub_service.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="user-service")
public interface UserFeignClient {
    // 예시
    @GetMapping("/user/{userId}")
    String getUser(@PathVariable("userId") String userId);
}

// FeignClient 인터페이스를 작성해서 서비스 호출을 수행
// User 관련된 서비스 받아와야 되어서 user-service 호출
//
// user-service를 FeingClient 어노테이션을 주고 interface로 선언
// 그 후, 엔드포인트를 지정해서 어떤 요청 정보가 있고 어떤 식으로 리턴 받을지 정의
// http://user-service/endpoint?param=