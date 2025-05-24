package javachip.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ORDERALARM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderAlarm {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_alarm_seq_gen")
    @SequenceGenerator(name = "order_alarm_seq_gen", sequenceName = "order_alarm_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alarm_id", nullable = false)
    private Alarm alarm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}

