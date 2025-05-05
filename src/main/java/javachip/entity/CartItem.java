package javachip.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CARTITEM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
