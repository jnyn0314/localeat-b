package javachip.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "\"Consumer\"")
// JOINED 전략에서는 자식 테이블의 PK가 부모 테이블의 PK를 참조하는 FK 역할도 겸합니다.
@PrimaryKeyJoinColumn(name = "user_id") // 부모 테이블 User의 PK(user_id)와 조인됨을 명시
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue("CONSUMER")
public class Consumer extends User {

    @Column(name = "birth", nullable = false, length = 20)
    private String birth;

    // Cart와의 관계 설정 (Consumer 테이블에 cart_id FK가 있으므로 Consumer가 연관관계 주인)
    @OneToOne(fetch = FetchType.LAZY) // 필요에 따라 EAGER로 변경 가능
    @JoinColumn(name = "cart_id") // Consumer 테이블의 FK 컬럼명
    private Cart cart; // Cart 엔티티가 정의되어 있어야 합니다.

    // Lombok @SuperBuilder를 사용하면 부모 필드를 포함하는 빌더 생성

}