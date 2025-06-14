/*
파일명 : Product.java
파일설명 : Product 엔티티.
작성자 : 정여진
기간 : 2025-05.01.
*/
package javachip.entity.product;

import jakarta.persistence.*;
import javachip.entity.user.Seller;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Transient;

import java.util.Date;
import java.util.List;

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
    private String productName;

    private Integer price;

    @Transient
    private Float subscriptionDiscountRate = 0.2f;
    @Transient
    private Integer deliveryFee = 3000;

    @Column(name = "is_group_buy", nullable = false)
    private Boolean isGroupBuy;

    @Enumerated(EnumType.STRING)
    private LocalType local;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_grade")
    private GradeBOption productGrade;

    private String description;

    @Column(name = "subscription_id")
    private Long subscriptionId;

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Column(name = "alarm_id")
    private Long alarmId;

    //김소망이 수정
    @CreationTimestamp
    @Column(name = "CREATE_AT")
    private Date createdAt;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    // 변경: sellerId를 삭제하고 seller를 추가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductImage> productImages;
}
