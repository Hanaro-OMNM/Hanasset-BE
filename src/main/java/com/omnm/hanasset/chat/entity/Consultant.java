package com.omnm.hanasset.chat.entity;

import lombok.*;

import jakarta.persistence.*;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "consultant")
public class Consultant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long consultantId;

    private String consultantName;
    private String profileImgUrl;
}