package javachip.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Transient;

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
    @MapsId("cartItemId")//GroupBuyCartItemId의 cartItemId와 연결
    @JoinColumn(name = "cartItemId")
    private CartItem cartItem;

    @ManyToOne
    @MapsId("groupBuyId")// @MapsId는 GroupBuyCartItemId의 groupBuyId와 연결
    @JoinColumn(name = "groupBuyId")
    private GroupBuy groupBuy;


    @Transient //이거 나중에 지워야함 이거 있음 db에 안들어감
    private LocalDateTime addedAt;

    @Transient
    private LocalDateTime expiresAt;

    // 생성 시 자동 계산
    /*@PrePersist
    public void prePersist() {
        this.addedAt = LocalDateTime.now();
        this.expiresAt = this.addedAt.plusHours(24);
    }*/
}
