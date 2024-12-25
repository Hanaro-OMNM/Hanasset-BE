package com.omnm.hanasset.bookmark.repository;

import com.omnm.hanasset.bookmark.entity.BookmarkArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkAreaRepository extends JpaRepository<BookmarkArea, Long> {}
