/*
파일명 : ProductImage.java
파일설명 : ProductImage 테이블 엔티티 (상품 대표사진을 db에 넣기 위함입니다.)
작성자 : 정여진
기간 : 2025-05.03.
*/
package javachip.entity.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "PRODUCTIMAGE")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_image_seq_gen")
    @SequenceGenerator(name = "product_image_seq_gen", sequenceName = "PRODUCTIMAGE_SEQ", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private String imageName;   // 이전: image_name

    @Lob
    @Column(name = "image_data", columnDefinition = "CLOB")
    private String imageData;  // Base64로 인코딩된 문자열

    @Column(name = "image_type")
    private String imageType;
}
