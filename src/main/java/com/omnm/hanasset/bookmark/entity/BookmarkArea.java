package com.omnm.hanasset.bookmark.entity;

import com.omnm.hanasset.areaCode.entity.AreaCode;
import com.omnm.hanasset.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookmark_area")
public class BookmarkArea {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_area_id")
    private Long bookmarkAreaId;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_code_id", nullable = false)
    private AreaCode areaCode;

}
