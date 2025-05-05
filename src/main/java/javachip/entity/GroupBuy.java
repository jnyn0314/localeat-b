package javachip.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "GROUPBUY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupBuy {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "groupbuy_seq_gen")
    @SequenceGenerator(name = "groupbuy_seq_gen", sequenceName = "GROUPBUY_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id; // groupBuyId

    @OneToOne
    @JoinColumn(name = "PRODUCT_ID", nullable = false, unique = true)
    private Product product;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DEADLINE")
    private LocalDate deadline;

    @Column(name = "PARTI_COUNT")
    private Integer partiCount = 0;

    @Column(name = "PAY_COUNT")
    private Integer payCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private GroupBuyStatus status;

    @Column(name = "TIME")
    private LocalDateTime time;

    @OneToMany(mappedBy = "groupBuy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();

    @OneToOne
    @Column(name = "ORDER_ITEM_ID")
    private OrderItem orderItem;

    /*@OneToOne
    @Column(name = "GB_ALARM_ID")
    private GroupBuyAlarm gbAlarm;
*/
}
