package javachip.dto.order;

import javachip.entity.OrderItem;
import lombok.Getter;

@Getter
public class OrderCreateResponse {
    private Long orderItemId;
    private String productName;
    private int quantity;
    private int price;
    private String status;

    public OrderCreateResponse(OrderItem orderItem) {
        this.orderItemId = orderItem.getId();
        this.productName = orderItem.getProduct().getProductName();
        this.quantity = orderItem.getQuantity();
        this.price = orderItem.getPrice();
        this.status = orderItem.getStatus().name();
    }
}
