package com.omnm.hanasset.bookmark.service;

import com.omnm.hanasset.areaCode.repository.AreaCodeRepository;
import com.omnm.hanasset.bookmark.dto.AreaCodeDto;
import com.omnm.hanasset.bookmark.dto.BookmarkAreaCodesResponse;
import com.omnm.hanasset.bookmark.dto.BookmarkRealEstatesResponse;
import com.omnm.hanasset.bookmark.entity.BookmarkArea;
import com.omnm.hanasset.bookmark.entity.BookmarkRealEstate;
import com.omnm.hanasset.bookmark.repository.BookmarkAreaRepository;
import com.omnm.hanasset.bookmark.repository.BookmarkRealEstateRepository;
import com.omnm.hanasset.realEstate.dto.RealEstateDto;
import com.omnm.hanasset.realEstate.repository.RealEstateRepository;
import com.omnm.hanasset.realEstate.utils.RealEstateMapper;
import com.omnm.hanasset.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {
    private final BookmarkRealEstateRepository bookmarkRealEstateRepository;
    private final BookmarkAreaRepository bookmarkAreaRepository;
    private final RealEstateRepository realEstateRepository;
    private final AreaCodeRepository areaCodeRepository;
    private final UserRepository userRepository;

    private final RealEstateMapper realEstateMapper;


    public BookmarkRealEstatesResponse getBookmarkRealEstates(Long userId) {
        List<BookmarkRealEstate> bookmarks = bookmarkRealEstateRepository.findByUser_UserId(userId);
        List<RealEstateDto> realEstateDtos = bookmarks.stream()
                .map(bookmark -> realEstateMapper.toRealEstateDto(bookmark.getRealEstate()))
                .collect(Collectors.toList());

        return BookmarkRealEstatesResponse.builder()
                .realEstates(realEstateDtos)
                .build();
    }

    @Transactional
    public void addBookmarkRealEstate(Long userId, Long realEstateId) {
        // TODO: 추가할 매물이 기존에 북마크한 매물인지 확인 및 예외처리
        BookmarkRealEstate bookmark = BookmarkRealEstate.builder()
                .user(userRepository.findById(userId).orElseThrow())
                .realEstate(realEstateRepository.findById(realEstateId).orElseThrow())
                .build();

        bookmarkRealEstateRepository.save(bookmark);
    }

    @Transactional
    public void deleteBookmarkRealEstate(Long userId, Long realEstateId) {
        //TODO: 삭제할 매물이 기존에 북마크한 매물인지 확인 및 예외처리
        bookmarkRealEstateRepository.deleteByUser_UserIdAndRealEstate_RealEstateId(userId, realEstateId);
    }

    public BookmarkAreaCodesResponse getBookmarkAreaCodes(Long userId) {
        List<BookmarkArea> bookmarks = bookmarkAreaRepository.findByUser_UserId(userId);

        List<AreaCodeDto> areaCodeDtos = bookmarks.stream()
                .map(bookmark -> AreaCodeDto.builder()
                        .codeId(bookmark.getAreaCode().getCode())
                        .emdName(bookmark.getAreaCode().getEmdName())
                        .centerLat(bookmark.getAreaCode().getCoordinate().getY())
                        .centerLng(bookmark.getAreaCode().getCoordinate().getX())
                        .build())
                .collect(Collectors.toList());

        return BookmarkAreaCodesResponse.builder()
                .areaCodes(areaCodeDtos)
                .build();
    }

    @Transactional
    public void addBookmarkAreaCode(Long userId, Long codeId) {
        //TODO: 추가할 지역 코드가 기존에 북마크한 지역 코드인지 확인 및 예외처리
        BookmarkArea bookmark = BookmarkArea.builder()
                .user(userRepository.findById(userId).orElseThrow())
                .areaCode(areaCodeRepository.findByCode(codeId).orElseThrow())
                .build();

        bookmarkAreaRepository.save(bookmark);
    }

    @Transactional
    public void deleteBookmarkAreaCode(Long userId, Long codeId) {
        //TODO: 삭제할 지역 코드가 기존에 북마크한 지역 코드인지 확인 및 예외처리
        bookmarkAreaRepository.deleteByUser_UserIdAndAreaCode_Code(userId, codeId);
    }
}
