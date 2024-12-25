package com.omnm.hanasset.bookmark.repository;

import com.omnm.hanasset.bookmark.entity.BookmarkRealEstate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRealEstateRepository extends JpaRepository<BookmarkRealEstate, Long> {
    List<BookmarkRealEstate> findByUser_UserId(Long userId);
}
