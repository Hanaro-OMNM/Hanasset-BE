package com.omnm.hanasset.bookmark.controller;

import com.omnm.hanasset.bookmark.dto.BookmarkRealEstatesResponse;
import com.omnm.hanasset.bookmark.service.BookmarkService;
import com.omnm.hanasset.global.common.ApiResponseEntity;
import com.omnm.hanasset.global.dto.UserDetailsDTO;
import com.omnm.hanasset.realEstate.dto.RealEstateDto;
import com.omnm.hanasset.realEstate.dto.RealEstatesResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "북마크", description = "북마크 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @GetMapping("/real-estates")
    public ApiResponseEntity<BookmarkRealEstatesResponse> getBookmarkRealEstates(@AuthenticationPrincipal UserDetailsDTO userDetailsDTO) {
        BookmarkRealEstatesResponse realEstates = bookmarkService.getBookmarkRealEstates(userDetailsDTO.getId());
        return ApiResponseEntity.ok("내 관심 매물 리스트 조회 성공", realEstates);
    }



}
