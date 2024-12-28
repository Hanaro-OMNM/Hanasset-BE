package com.omnm.hanasset.realEstate.repository;

import com.omnm.hanasset.realEstate.entity.HousingComplex;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.omnm.hanasset.realEstate.entity.QHousingComplex.housingComplex;

@RequiredArgsConstructor
public class HousingComplexRepositoryImpl implements HousingComplexRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<HousingComplex> searchWithKeyword(PageRequest pageRequest, String keyword) {

        List<HousingComplex> nameResults = queryFactory
                .selectFrom(housingComplex)
                .where(housingComplex.name.contains(keyword))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        List<HousingComplex> addressResults = queryFactory
                .selectFrom(housingComplex)
                .where(housingComplex.address.contains(keyword)
                        .and(housingComplex.housingComplexId.notIn(nameResults.stream().map(HousingComplex::getHousingComplexId).collect(Collectors.toList())))) // nameResults에 포함되지 않은 address 결과만 가져오기
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        List<HousingComplex> resultList = Stream.concat(nameResults.stream(), addressResults.stream())
                .distinct()
                .limit(10)
                .collect(Collectors.toList());

        return new PageImpl<>(resultList, pageRequest, resultList.size());
    }
}
