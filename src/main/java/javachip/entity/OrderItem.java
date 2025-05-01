/*
파일명 : Orders.java
파일설명 : Orders 테이블 엔티티
작성자 : 김민하
기간 : 2025-05.01.
*/
package javachip.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orderitem")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    private int quantity;

    private String status;

    // column 어노테이션 쓰던가 변수명 바꾸던가 해야해요!!
    private boolean isSubscription;

    private boolean isGroupbuy;

    private Long productId;

    private Long orderId;

    private String userId;
}