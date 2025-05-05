package javachip.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PARTICIPANT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(ParticipantId.class)
public class Participant {

    @Id
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private Consumer consumer;

    @Id
    @ManyToOne
    @JoinColumn(name = "groupBuyId", nullable = false)
    private GroupBuy groupBuy;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Boolean payment = false;

    // 상품 정보도 필요한 경우
    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;
}
