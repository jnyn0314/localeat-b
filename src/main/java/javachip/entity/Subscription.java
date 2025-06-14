package javachip.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "SUBSCRIPTION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subscription_seq_gen")
    @SequenceGenerator(name = "subscription_seq_gen", sequenceName = "SUBSCRIPTION_SEQ", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "DELIVERY_CYCLE")
    private DeliveryCycle deliveryCycle;

    @Column(name = "DELIVERY_PERIOD")
    private LocalDate deliveryPeriod;

    @Column(name = "SUBSCRIBED_AT")
    private LocalDate subscribedAt;

    @Column(name = "NEXT_DELIVERY_DATE")
    private LocalDate nextDeliveryDate;

    @Column(name = "QUANTITY")
    private int quantity;

    @OneToOne
    @JoinColumn(name = "ORDER_ITEM_ID")
    private OrderItem orderItem;

    @Column(name = "PERIOD_IN_MONTHS")
    private Integer periodInMonths;

    @Column(name = "DELIVERY_CYCLE_VALUE")
    private int deliveryCycleValue;

    @Column(name = "USER_ID")
    private String userId;
}