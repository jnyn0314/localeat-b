/*
파일명 : Product.java
파일설명 : Product 테이블 엔티티
작성자 : 정여진
기간 : 2025-05.01.

김소망이 수정함
*/
package javachip.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "CART")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "cart", fetch = FetchType.LAZY)
    private Consumer consumer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> items;
}
