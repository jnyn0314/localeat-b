/*
파일명 : Product.java
파일설명 : Product 테이블 엔티티
작성자 : 정여진
기간 : 2025-05.01.
*/
package javachip.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private int product_id;

    private String product_name;

    private Integer price;

    private Float grade_discount_rate;

    private Float subscription_discount_rate;

    private Boolean is_subscription;

    private boolean is_group_buy;

    private String local;

    private String product_grade;

    private Integer delivery_fee;

    private String description;

    // private Subscription subscription;
    // private GroupBuy groupBuy;

    private Integer maxParticipants;

    private String images;

    private String order_items;

    private Date create_at;

    private Integer stock_quantity;
}
