/*
파일명 : Product.java
파일설명 : Product 엔티티.
작성자 : 정여진
기간 : 2025-05.01.
*/
package javachip.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "Product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq_gen")
    @SequenceGenerator(name = "product_seq_gen", sequenceName = "PRODUCT_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;              // 이전: product_name

    private Integer price;

    @Column(name = "grade_discount_rate")
    private Float gradeDiscountRate;         // 이전: grade_discount_rate

    @Column(name = "subscription_discount_rate")
    private Float subscriptionDiscountRate;  // 이전: subscription_discount_rate

    @Column(name = "is_subscription")
    private Boolean isSubscription;          // 이전: is_subscription

    @Enumerated(EnumType.STRING)
    private LocalType local;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_grade")
    private GradeBOption productGrade;       // 이전: product_grade

    @Column(name = "delivery_fee")
    private Integer deliveryFee;             // 이전: delivery_fee

    private String description;

    @Column(name = "subscription_id")
    private Long subscriptionId;             // 이전: subscription_id

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "is_group_buy")
    private GroupBuyOption groupBuy;         // 이전: is_group_buy

    @Column(name = "max_participants")
    private Integer maxParticipants;         // 이전: max_participants

    @Column(name = "alarm_id")
    private Long alarmId;                    // 이전: alarm_id

    @Column(name = "create_at")
    private Date createAt;                   // 이전: create_at

    @Column(name = "stock_quantity")
    private Integer stockQuantity;           // 이전: stock_quantity

    @Column(name = "seller_id")
    private String sellerId;                 // 이전: seller_id
}
