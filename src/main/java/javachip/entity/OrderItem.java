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
@Table(name = "OrderItem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int order_item_id;

    @Column(nullable = false)
    private int quantity;

    /** 주문 상태 (PAID, READY, DELIVERING, DELIVERED) */
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(nullable = false)
    private boolean is_subscription;

    @Column(nullable = false)
    private boolean is_group_buy;

    /** 연관된 상품 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product_id;

    /** 이 항목이 속한 주문 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

//    /** 주문자(사용자) ID */
//    @Column(name = "id", nullable = false, length = 20)
//    private String userId;
}