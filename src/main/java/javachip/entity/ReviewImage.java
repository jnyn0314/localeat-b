/*
파일명 : ReviewImage.java
파일설명 : ReviewImage 테이블 엔티티
작성자 : 김민하
기간 : 2025-05.03.
*/
package javachip.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviewimage")
@Builder
public class ReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_image_seq")
    @SequenceGenerator(name = "review_image_seq", sequenceName = "review_image_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;
}
