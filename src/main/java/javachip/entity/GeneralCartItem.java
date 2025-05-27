package javachip.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "GENERALCARTITEM")
public class GeneralCartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generalcartitem_seq")
    @SequenceGenerator(name = "generalcartitem_seq", sequenceName = "GENERALCARTITEM_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;


    @OneToOne
    @JoinColumn(name = "cart_item_id", unique = true)
    private CartItem cartItem;

}
