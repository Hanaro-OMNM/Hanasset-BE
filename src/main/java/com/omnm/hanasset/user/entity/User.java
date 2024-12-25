package com.omnm.hanasset.user.entity;

import com.omnm.hanasset.bookmark.entity.BookmarkArea;
import com.omnm.hanasset.bookmark.entity.BookmarkRealEstate;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name = "user")
public class User {
    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user")
    private List<BookmarkRealEstate> bookmarkRealEstates;

    @OneToMany(mappedBy = "user")
    private List<BookmarkArea> bookmarkAreas;

    public void updateBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
