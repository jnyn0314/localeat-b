/*
파일명 : Wish.java
파일설명 : 사용자(User)가 찜한 상품(Product)을 저장하는 엔티티 클래스입니다.
작성자 : 정여진
작성일 : 2025-05-17.
*/
package javachip.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wish {
    // PK: 찜 고유 ID
    @Id
    @GeneratedValue
    private Long id;

    // 찜한 사용자 (ManyToOne: 사용자 1명이 여러 찜 가능)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 찜한 상품 (ManyToOne: 상품 하나를 여러 명이 찜 가능)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // 찜 생성 시각 (기본값 현재 시각)
    private LocalDateTime createdAt;

    // 생성 시 자동으로 날짜 입력
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}