package javachip.dto.order;

import javachip.entity.CartItem;
import javachip.entity.GradeBOption;
import javachip.entity.Product;
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
        int finalPrice = product.getProductGrade() == GradeBOption.B
                ? (int) Math.floor(product.getPrice() * (1 - product.getGradeDiscountRate()))
                : product.getPrice();

        this.cartItemId = item.getCartItemId();
        this.productId = product.getId();
        this.productName = product.getProductName();
        this.price = finalPrice;
        this.quantity = item.getQuantity();
    }

    public static CartItemResponse fromEntity(CartItem item) {
        return new CartItemResponse(item);
    }
}
