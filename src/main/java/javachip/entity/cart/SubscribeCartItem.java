/*
파일명: SubscribeCartItem.java
설명: 구독 장바구니 항목 엔티티. CartItem을 상속받아 구독 전용 필드(배송 주기 등)를 확장함.
작성자: 정여진
작성일: 2025.05.29.
*/
package javachip.entity.cart;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import javachip.entity.subscribe.Subscription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "SUBSCRIBE_CART_ITEM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SubscribeCartItem extends CartItem {

    private String deliveryCycle; // ex: 1주, 2개월 등

    @ManyToOne
    @JoinColumn(name = "SUBSCRIBE_ID")
    private Subscription subscription; // 구독 관련 엔티티
}