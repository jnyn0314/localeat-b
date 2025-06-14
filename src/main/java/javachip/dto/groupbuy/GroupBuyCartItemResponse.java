package javachip.dto.groupbuy;

import javachip.entity.cart.GroupBuyCartItem;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GroupBuyCartItemResponse {
    private Long     cartItemId;
    private Long     groupBuyId;
    private Long     productId;
    private int      quantity;
    private String   paymentStatus;
    private LocalDateTime addedAt;
    private LocalDateTime expiresAt;

    public static GroupBuyCartItemResponse fromEntity(GroupBuyCartItem e) {
        var r = new GroupBuyCartItemResponse();
        r.cartItemId    = e.getId();
        r.groupBuyId    = e.getGroupBuy().getId().longValue();
        r.productId     = e.getGroupBuy().getProduct().getId();
        r.quantity      = e.getCartItem().getQuantity();
        r.paymentStatus = e.getPaymentStatus().name();
        r.addedAt       = e.getAddedAt();
        r.expiresAt     = e.getExpiresAt();
        return r;
    }
}