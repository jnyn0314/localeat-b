package javachip.dto.order.consumer;

import javachip.entity.cart.CartItem;
import javachip.entity.product.Product;
import lombok.Getter;

@Getter
public class CartItemResponse {
    private Long cartItemId;
    private Long productId;
    private String productName;
    private int price;
    private int quantity;

    public CartItemResponse(CartItem item) {
        Product product = item.getProduct();

        this.cartItemId = item.getCartItemId();
        this.productId = product.getId();
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.quantity = item.getQuantity();
    }

    public static CartItemResponse fromEntity(CartItem item) {
        return new CartItemResponse(item);
    }
}
