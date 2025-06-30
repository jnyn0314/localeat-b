package javachip.entity.cart;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class GroupBuyCartItemId implements Serializable {
    private Long cartItemId;
    private Long groupBuyId;

    // Getter, Setter, Constructor 등 필요한 코드
    public GroupBuyCartItemId() {}

    public GroupBuyCartItemId(Long cartItemId, Long groupBuyId) {
        this.cartItemId = cartItemId;
        this.groupBuyId = groupBuyId;
    }
}
