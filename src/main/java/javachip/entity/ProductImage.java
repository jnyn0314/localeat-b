/*
파일명 : ProductImage.java
파일설명 : ProductImage 테이블 엔티티 (상품 대표사진을 db에 넣기 위함입니다.)
작성자 : 정여진
기간 : 2025-05.03.
*/
package javachip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long product_id;

    private String image_name;

    private byte[] image_data;
}
