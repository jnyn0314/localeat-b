package javachip.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inquiry_seq_gen")
    @SequenceGenerator(name = "inquiry_seq_gen", sequenceName = "INQUIRY_SEQ", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Consumer consumer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InquiryCategory category;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Lob
    private String answer;

    private LocalDateTime answeredAt;

    /** 작성일 자동 설정 */
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
