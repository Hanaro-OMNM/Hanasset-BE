package com.omnm.hanasset.user.controller;

import com.omnm.hanasset.global.common.ApiResponseEntity;
import com.omnm.hanasset.global.dto.UserDetailsDTO;
import com.omnm.hanasset.user.dto.UserPropertyResponse;
import com.omnm.hanasset.user.service.UserPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserPropertyController {
    private final UserPropertyService userPropertyService;

    @GetMapping("/assets")
    public ApiResponseEntity<UserPropertyResponse> getUserPropertyInfo(@AuthenticationPrincipal UserDetailsDTO userDetailsDTO) {
        Long userId = userDetailsDTO.getId();
        UserPropertyResponse userPropertyInfo = userPropertyService.getUserPropertyInfo(userId);

        return ApiResponseEntity.ok("회원 자산 불러오기 성공", userPropertyInfo);
    }

    @GetMapping("/test")
    public ApiResponseEntity<?> setTest() {
        return ApiResponseEntity.ok("테스트", null);
    }
}
