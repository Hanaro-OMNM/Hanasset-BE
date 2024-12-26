package com.omnm.hanasset.chat.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "consulting_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consulting_item_id")
    private Long consultingItemId;

    @Column(name = "chatroom_id", nullable = false)
    private String chatroomId;

    @Column(name = "real_estate_id", nullable = false)
    private Long realEstateId;

    @Column(name = "housing_complex_id", nullable = false)
    private Long housingComplexId;
}