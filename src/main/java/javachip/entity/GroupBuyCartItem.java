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

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gbci_seq")
    @SequenceGenerator(name = "gbci_seq", sequenceName = "GROUPBUYCARTITEM_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CARTITEM_ID", nullable = false)
    private CartItem cartItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_BUY_ID", nullable = false)
    private GroupBuy groupBuy;

    @Column(name = "ADDED_AT", nullable = false)
    private LocalDateTime addedAt;

    @Column(name = "EXPIRES_AT", nullable = false)
    private LocalDateTime expiresAt;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_STATUS", nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @PrePersist
    public void prePersist() {
        this.addedAt   = LocalDateTime.now();
        this.expiresAt = this.addedAt.plusHours(24);
    }
}
