package com.omnm.hanasset.search.service;

import com.omnm.hanasset.realEstate.repository.HousingComplexRepository;
import com.omnm.hanasset.search.dto.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SearchService {

    private final HousingComplexRepository housingComplexRepository;

    public SearchResponse search(String query) {
        return SearchResponse.builder().build();
    }
}
