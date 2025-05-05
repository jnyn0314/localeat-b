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
@Table(name = "PRODUCTIMAGE")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productImageSeq")
    @SequenceGenerator(name = "productImageSeq", sequenceName = "PRODUCT_IMAGE_SEQ", allocationSize = 1)
    private Long id;

    @JoinColumn(name = "product_id", nullable = false)
    private Long productId; // ← 필드명 CamelCase로 고침

    private String image_name;

    private byte[] image_data;

    @Version
    private int version;
}
