package javachip.dto.groupbuy;

import javachip.entity.GroupBuyCartItem;
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
        r.cartItemId    = e.getCartItemId();
        r.groupBuyId    = e.getGroupBuy().getId().longValue();
        r.productId     = e.getProduct().getId();
        r.quantity      = e.getQuantity();
        r.paymentStatus = e.getPaymentStatus().name();
        r.addedAt       = e.getAddedAt();
        r.expiresAt     = e.getExpiresAt();
        return r;
    }
}