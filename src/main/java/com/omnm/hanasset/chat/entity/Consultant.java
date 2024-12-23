package com.omnm.hanasset.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "consultant")
public class Consultant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long consultantId;

    private String consultantName;
    private String profileImgUrl;
}