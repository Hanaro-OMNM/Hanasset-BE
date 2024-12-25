package com.omnm.hanasset.user.controller;

import com.omnm.hanasset.global.common.ApiResponseEntity;
import com.omnm.hanasset.global.dto.UserDetailsDTO;
import com.omnm.hanasset.user.dto.PropertyResponse;
import com.omnm.hanasset.user.service.UserPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserPropertyController {
    private final UserPropertyService userPropertyService;

    @GetMapping("/property")
    public ApiResponseEntity<PropertyResponse> getUserPropertyInfo(@AuthenticationPrincipal UserDetailsDTO userDetailsDTO) {
        Long userId = userDetailsDTO.getId();

        PropertyResponse userPropertyInfo = userPropertyService.getUserPropertyInfo(userId);

        return ApiResponseEntity.ok("자산 정보 불러오기 성공", userPropertyInfo);
    }

    @PutMapping("/property")
    public ApiResponseEntity<?> updateUserPropertyInfo(@AuthenticationPrincipal UserDetailsDTO userDetailsDTO, @RequestParam("type") String type, @RequestParam("value") String value) {
        Long userId = userDetailsDTO.getId();

        userPropertyService.updateUserPropertyInfo(userId, type, value);

        return ApiResponseEntity.ok("자산 정보 등록하기 성공", null);
    }
}
