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
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cart_seq_gen"
    )
    @SequenceGenerator(
            name = "cart_seq_gen",
            sequenceName = "CART_SEQ",
            allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @OneToOne(mappedBy = "cart", fetch = FetchType.LAZY)
    private Consumer consumer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> items;

    public Cart(Consumer consumer) {
        this.consumer = consumer;
    }
}
