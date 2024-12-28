package com.omnm.hanasset.search.service;

import com.omnm.hanasset.realEstate.entity.HousingComplex;
import com.omnm.hanasset.realEstate.repository.HousingComplexRepository;
import com.omnm.hanasset.search.dto.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.locationtech.jts.geom.Point;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SearchService {

    private final HousingComplexRepository housingComplexRepository;

    public List<SearchResponse> search(PageRequest pageRequest, String query) {
        Page<HousingComplex> results = housingComplexRepository.searchWithKeyword(pageRequest, query);

        return results.map(result -> {
            Point coordinate = result.getAreaCode().getCoordinate();
                    return SearchResponse.builder()
                            .housingComplexId(result.getHousingComplexId())
                            .complexName(result.getName())
                            .addressName(result.getAddress())
                            .lat(coordinate.getY())
                            .lng(coordinate.getX())
                            .build();
                })
                .toList();
    }
}
