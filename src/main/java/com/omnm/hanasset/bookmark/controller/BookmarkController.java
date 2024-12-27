package com.omnm.hanasset.bookmark.controller;

import com.omnm.hanasset.bookmark.dto.BookmarkAreaCodesResponse;
import com.omnm.hanasset.bookmark.dto.BookmarkRealEstatesResponse;
import com.omnm.hanasset.bookmark.dto.BookmarkResponse;
import com.omnm.hanasset.bookmark.service.BookmarkService;
import com.omnm.hanasset.global.common.ApiResponseEntity;
import com.omnm.hanasset.global.dto.UserDetailsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Tag(name = "북마크", description = "북마크 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @Operation(summary = "지역, 매물 북마크 조회", description = "내 관심 매물과 지역 리스트를 조회한다.")
    @ApiResponse(responseCode = "200", description = "북마크 조회 성공")
    @GetMapping
    public ApiResponseEntity<BookmarkResponse> getBookmarks(@AuthenticationPrincipal UserDetailsDTO userDetailsDTO) {
        BookmarkAreaCodesResponse bookmarkAreaCodes = bookmarkService.getBookmarkAreaCodes(userDetailsDTO.getId());
        BookmarkRealEstatesResponse bookmarkRealEstates = bookmarkService.getBookmarkRealEstates(userDetailsDTO.getId());

        return ApiResponseEntity.ok("북마크 조회 성공", BookmarkResponse.builder()
                .areaCodes(bookmarkAreaCodes.getAreaCodes())
                .realEstates(bookmarkRealEstates.getRealEstates())
                .build());
    }

    @Operation(summary = "매물 북마크 조회", description = "내 관심 매물 리스트를 조회한다.")
    @ApiResponse(responseCode = "200", description = "내 관심 매물 리스트 조회 성공")
    @GetMapping("/real-estates")
    public ApiResponseEntity<BookmarkRealEstatesResponse> getBookmarkRealEstates(@AuthenticationPrincipal UserDetailsDTO userDetailsDTO) {
        BookmarkRealEstatesResponse realEstates = bookmarkService.getBookmarkRealEstates(userDetailsDTO.getId());
        return ApiResponseEntity.ok("내 관심 매물 리스트 조회 성공", realEstates);
    }

    @Operation(summary = "매물 북마크 추기", description = "내 관심 매물 리스트를 추가한다.")
    @ApiResponse(responseCode = "200", description = "내 관심 매물 리스트 추가 성공")
    @PostMapping("/real-estates/{realEstateId}")
    public ApiResponseEntity<Void> addBookmarkRealEstate(@AuthenticationPrincipal UserDetailsDTO userDetailsDTO, @PathVariable Long realEstateId) {
        bookmarkService.addBookmarkRealEstate(userDetailsDTO.getId(), realEstateId);
        return ApiResponseEntity.ok("내 관심 매물 북마크 추가 성공", null);
    }

    @Operation(summary = "매물 북마크 삭제", description = "내 관심 매물 리스트를 삭제한다.")
    @ApiResponse(responseCode = "200", description = "내 관심 매물 리스트 삭제 성공")
    @DeleteMapping("/real-estates/{realEstateId}")
    public ApiResponseEntity<Void> deleteBookmarkRealEstate(@AuthenticationPrincipal UserDetailsDTO userDetailsDTO, @PathVariable Long realEstateId) {
        bookmarkService.deleteBookmarkRealEstate(userDetailsDTO.getId(), realEstateId);
        return ApiResponseEntity.ok("내 관심 매물 북마크 삭제 성공", null);
    }

    @Operation(summary = "지역 북마크 조회", description = "내 관심 지역 리스트를 조회한다.")
    @ApiResponse(responseCode = "200", description = "내 관심 지역 리스트 조회 성공")
    @GetMapping("/area-codes")
    public ApiResponseEntity<BookmarkAreaCodesResponse> getBookmarkAreaCodes(@AuthenticationPrincipal UserDetailsDTO userDetailsDTO) {
        BookmarkAreaCodesResponse areaCodes = bookmarkService.getBookmarkAreaCodes(userDetailsDTO.getId());
        return ApiResponseEntity.ok("내 관심 지역 리스트 조회 성공", areaCodes);
    }

    @Operation(summary = "지역 북마크 추가", description = "내 관심 지역 리스트를 추가한다.")
    @ApiResponse(responseCode = "200", description = "내 관심 지역 리스트 추가 성공")
    @PostMapping("/area-codes/{codeId}")
    public ApiResponseEntity<Void> addBookmarkAreaCode(@AuthenticationPrincipal UserDetailsDTO userDetailsDTO, @PathVariable Long codeId) {
        bookmarkService.addBookmarkAreaCode(userDetailsDTO.getId(), codeId);
        return ApiResponseEntity.ok("내 관심 지역 북마크 추가 성공", null);
    }

    @Operation(summary = "지역 북마크 삭제", description = "내 관심 지역 리스트를 삭제한다.")
    @ApiResponse(responseCode = "200", description = "내 관심 지역 리스트 삭제 성공")
    @DeleteMapping("/area-codes/{codeId}")
    public ApiResponseEntity<Void> deleteBookmarkAreaCode(@AuthenticationPrincipal UserDetailsDTO userDetailsDTO, @PathVariable Long codeId) {
        bookmarkService.deleteBookmarkAreaCode(userDetailsDTO.getId(), codeId);
        return ApiResponseEntity.ok("내 관심 지역 북마크 삭제 성공", null);
    }
}
