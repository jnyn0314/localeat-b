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
    private String product_name;

    private Integer price;

    private Float grade_discount_rate;

    private Float subscription_discount_rate;

    private Boolean is_subscription;

    @Enumerated(EnumType.STRING)
    private LocalType local;

    @Enumerated(EnumType.STRING)
    private GradeBOption product_grade;

    private Integer delivery_fee;

    private String description;

    private Long subscription_id;

    @Enumerated(EnumType.ORDINAL)
    private GroupBuyOption is_group_buy;

    private Integer max_participants;

    private Long alarm_id;

    private Date create_at;

    private Integer stock_quantity;

    private String seller_id;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProductImage image_id;
}
