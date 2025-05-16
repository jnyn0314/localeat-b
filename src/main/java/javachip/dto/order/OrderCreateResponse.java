package javachip.dto.order;

import javachip.entity.OrderItem;
import lombok.Getter;

@Getter
public class OrderCreateResponse {
    private Long orderItemId;
    private String productName;
    private int quantity;
    private int originalPrice;
    private int finalPrice;
    private float gradeDiscountRate;
    private float subscriptionDiscountRate;
    private String status;

    public OrderCreateResponse(OrderItem orderItem) {
        this.orderItemId = orderItem.getId();
        this.productName = orderItem.getProduct().getProductName();
        this.quantity = orderItem.getQuantity();
        this.originalPrice = orderItem.getOriginalPrice();
        this.finalPrice = orderItem.getFinalPrice();
        this.gradeDiscountRate = orderItem.getGradeDiscountRate();
        this.subscriptionDiscountRate = orderItem.getSubscriptionDiscountRate();
        this.status = orderItem.getStatus().name();
    }
}
