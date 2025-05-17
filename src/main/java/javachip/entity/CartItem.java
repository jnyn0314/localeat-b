package javachip.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "CARTITEM")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cartitem_seq")
    @SequenceGenerator(name = "cartitem_seq", sequenceName = "CARTITEM_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long cartItemId;

    private int quantity;
    private boolean isSelected;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "CART_ID")
    private Cart cart;
}
