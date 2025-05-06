/*
파일명 : Product.java
파일설명 : Product 엔티티.
작성자 : 정여진
기간 : 2025-05.01.
*/
package javachip.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "PRODUCT")
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

    @Column(name = "is_subscription", nullable = false)
    private boolean isSubscription;          // 이전: is_subscription

    @Column(name = "is_group_buy", nullable = false)
    private boolean isGroupBuy;         // 이전: is_group_buy

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

    @Column(name = "max_participants")
    private Integer maxParticipants;         // 이전: max_participants

    @Column(name = "alarm_id")
    private Long alarmId;                    // 이전: alarm_id

    //김소망이 수정
    @CreationTimestamp
    private LocalDateTime createdAt;        // 이전: create_at

    @Column(name = "stock_quantity")
    private Integer stockQuantity;           // 이전: stock_quantity

    @Column(name = "seller_id")
    private String sellerId;                 // 이전: seller_id

    //김소망이 추가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    //김소망이 추가
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private GroupBuy groupBuy;


}
