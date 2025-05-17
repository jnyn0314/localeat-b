/*
    파일명 : OrderItem.java
    파일설명 : OrderItem 테이블 엔티티
    작성자 : 김민하
    기간 : 2025-05.01.
*/
package javachip.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "ORDERITEM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @SequenceGenerator(name = "order_item_seq", sequenceName = "order_item_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_item_seq")
    private Long id;

    @Column(nullable = false)
    private int quantity;

    /** 주문 상태 (PAID, READY, DELIVERING, DELIVERED) */
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "is_subscription",  nullable = false)
    private boolean isSubscription;

    @Column(name = "is_group_buy", nullable = false)
    private boolean isGroupBuy;

    /** 연관된 상품 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /** 이 항목이 속한 주문 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    /** 주문자(사용자) ID */
    @Column(name = "user_id", nullable = false, length = 20)
    private String userId;

    @Column(name = "is_reviewed", nullable = false)
    private boolean isReviewed = false;

    @Column(name = "price")
    private Integer price;
}