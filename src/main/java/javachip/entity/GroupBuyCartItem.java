package javachip.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Transient;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "GROUPBUYCARTITEM")
@PrimaryKeyJoinColumn(name = "cartitem_id")//이것도 이름 고쳐야하고
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class GroupBuyCartItem extends CartItem{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_buy_id", nullable = false)
    private GroupBuy groupBuy;

    @Column(name = "added_at", nullable = false)
    private LocalDateTime addedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @PrePersist
    public void prePersist() {
        this.addedAt   = LocalDateTime.now();
        this.expiresAt = this.addedAt.plusHours(24);
    }
}
