package com.omnm.hanasset.bookmark.entity;

import com.omnm.hanasset.realEstate.entity.RealEstate;
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
@Table(name = "bookmark_real_estate")
public class BookmarkRealEstate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_real_estate_id")
    private Long bookmarkRealEstateId;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "real_estate_id", nullable = false)
    private RealEstate realEstate;

}
