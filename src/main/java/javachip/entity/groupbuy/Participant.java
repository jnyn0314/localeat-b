package javachip.entity.groupbuy;

import jakarta.persistence.*;
import javachip.entity.product.Product;
import javachip.entity.cart.Consumer;
import lombok.*;

@Entity
@Table(name = "PARTICIPANT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "participant_seq")
    @SequenceGenerator(name = "participant_seq", sequenceName = "PARTICIPANT_SEQ", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private Consumer consumer;

    @ManyToOne
    @JoinColumn(name = "groupBuyId", nullable = false)
    private GroupBuy groupBuy;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Boolean payment = false;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;
}
