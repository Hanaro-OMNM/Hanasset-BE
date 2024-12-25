package com.omnm.hanasset.user.controller;

import com.omnm.hanasset.global.common.ApiResponseEntity;
import com.omnm.hanasset.global.dto.UserDetailsDTO;
import com.omnm.hanasset.user.dto.PropertyResponse;
import com.omnm.hanasset.user.service.UserPropertyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "자산", description = "자산 API 목록")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserPropertyController {
    private final UserPropertyService userPropertyService;

    @Operation(summary = "자산 정보 조회하기", description = "유저의 자산 정보(직업, 연소득 등) 조회를 시도한다.")
    @ApiResponse(responseCode = "200", description = "자산 정보 불러오기 성공")
    @GetMapping("/property")
    public ApiResponseEntity<PropertyResponse> getUserPropertyInfo(@AuthenticationPrincipal UserDetailsDTO userDetailsDTO) {
        Long userId = userDetailsDTO.getId();

        PropertyResponse userPropertyInfo = userPropertyService.getUserPropertyInfo(userId);

        return ApiResponseEntity.ok("자산 정보 불러오기 성공", userPropertyInfo);
    }

    @Operation(summary = "자산 정보 등록 및 수정하기", description = "유저의 자산 정보를 새롭게 등록하거나 수정을 시도한다.")
    @ApiResponse(responseCode = "200", description = "자산 정보 등록하기 성공")
    @PutMapping("/property")
    public ApiResponseEntity<?> updateUserPropertyInfo(@AuthenticationPrincipal UserDetailsDTO userDetailsDTO, @RequestParam("type") String type, @RequestParam("value") String value) {
        Long userId = userDetailsDTO.getId();

        userPropertyService.updateUserPropertyInfo(userId, type, value);

        return ApiResponseEntity.ok("자산 정보 등록하기 성공", null);
    }
}
