package javachip.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "GROUPBUYCARTITEM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupBuyCartItem {
    @EmbeddedId
    private GroupBuyCartItemId id;

    @OneToOne
    @MapsId("cartItemId")
    @JoinColumn(name = "cartItemId")
    private CartItem cartItem;

    @ManyToOne
    @MapsId("groupBuyId")
    @JoinColumn(name = "groupBuyId")
    private GroupBuy groupBuy;

    @Column(nullable = false)
    private LocalDateTime addedAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    // 생성 시 자동 계산
    @PrePersist
    public void prePersist() {
        this.addedAt = LocalDateTime.now();
        this.expiresAt = this.addedAt.plusHours(24);
    }
}
