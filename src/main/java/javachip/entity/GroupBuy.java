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
    private Long id; // groupBuyId

    @OneToOne
    @JoinColumn(name = "productId", nullable = false, unique = true)
    private Product product;

    @Column(length = 100)
    private String description;

    @Column(nullable = false)
    private LocalDate deadline;

    @Column(nullable = false)
    private Integer partiCount = 0;

    @Column(nullable = false)
    private Integer payCount = 0;

    @Enumerated(EnumType.STRING)
    private GroupBuyStatus status;

    private LocalDateTime expiresAt;

    @OneToMany(mappedBy = "groupBuy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();

    // 필요시 알림, 주문 항목 연관관계도 추가
    // @OneToOne
    // private OrderItem orderItem;

    // @OneToOne
    // private GroupBuyAlarm gbAlarm;

}
