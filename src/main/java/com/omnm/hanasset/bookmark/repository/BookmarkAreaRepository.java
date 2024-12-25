package com.omnm.hanasset.bookmark.repository;

import com.omnm.hanasset.bookmark.entity.BookmarkArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkAreaRepository extends JpaRepository<BookmarkArea, Long> {
    List<BookmarkArea> findByUser_UserId(Long userId);
    void deleteByUser_UserIdAndAreaCode_AreaCodeId(Long userId, Long areaCodeId);
}
