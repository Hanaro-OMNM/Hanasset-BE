package com.omnm.hanasset.consultant.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name = "consultant")
public class Consultant {
    @Id
    @Column(name = "consultant_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long consultantId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "consultant_login_id", nullable = false, unique = true)
    private String consultantLoginId;

    @Column(name = "password", nullable = false)
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
