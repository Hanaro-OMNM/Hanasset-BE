package com.omnm.hanasset.chat.entity;

import com.omnm.hanasset.realEstate.entity.RealEstate;
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
    @Column(name = "consulting_item_id", nullable = false)
    private Long consultingItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id", nullable = false)
    private ChatRoom chatroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "real_estate_id", nullable = false)
    private RealEstate realEstate;

}