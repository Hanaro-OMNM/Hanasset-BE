package com.omnm.hanasset.search.controller;

import com.omnm.hanasset.global.common.ApiResponseEntity;
import com.omnm.hanasset.search.dto.SearchResponse;
import com.omnm.hanasset.search.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "검색", description = "검색 API 목록")
@RequiredArgsConstructor
@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "키워드로 매물 검색 결과 조회하기", description = "keyword로 매물 검색를 시도한다.")
    @ApiResponse(responseCode = "200", description = "검색 결과 조회 성공")
    @GetMapping
    public ApiResponseEntity<SearchResponse> getUserInfo(@RequestParam("keyword") String keyword ) {
        SearchResponse searchResponse = searchService.search(keyword);

        return ApiResponseEntity.ok("회원 정보 조회 성공", SearchResponse.builder().build());
    }
}
