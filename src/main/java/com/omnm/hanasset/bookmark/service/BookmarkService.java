package com.omnm.hanasset.bookmark.service;

import com.omnm.hanasset.bookmark.dto.BookmarkRealEstatesResponse;
import com.omnm.hanasset.bookmark.entity.BookmarkRealEstate;
import com.omnm.hanasset.bookmark.repository.BookmarkAreaRepository;
import com.omnm.hanasset.bookmark.repository.BookmarkRealEstateRepository;
import com.omnm.hanasset.realEstate.dto.RealEstateDto;
import com.omnm.hanasset.realEstate.utils.RealEstateMapper;
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
}
