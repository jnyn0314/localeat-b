package javachip.dto.order.consumer;

import javachip.entity.CartItem;
import javachip.entity.Cart;
import javachip.entity.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemRequest {

    private Long productId;
    private int quantity;

    /**
     * @param cart 장바구니 엔티티
     * @param product 상품 엔티티
     */
    public CartItem toEntity(Cart cart, Product product) {
        return CartItem.builder()
                .product(product)
                .quantity(quantity)
                .isSelected(true)
                .cart(cart)
                .build();
    }
}
