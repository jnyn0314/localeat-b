/*
    파일명 : User.java
    파일설명 : User 테이블 엔티티
    작성자 : 김소망
    기간 : 2025-05.03.
*/
package javachip.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder; // 상속 관계 빌더 사용 시 필요
import javachip.enums.UserRole;
import javachip.enums.LocalType;

@Entity
@Table(name = "\"User\"") // Oracle에서 대소문자 구분 테이블 이름 사용 시 따옴표 필요
@Inheritance(strategy = InheritanceType.JOINED) // 조인 전략 사용
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
// DiscriminatorColumn은 JOINED 전략에서 필수는 아니지만, 명시적으로 구분자 컬럼을 지정할 수도 있습니다.
// @DiscriminatorColumn(name = "role") // User 테이블의 role 컬럼을 구분자로 사용
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder // 상속 관계에서 빌더 패턴을 사용하려면 @SuperBuilder 사용
public class User {

    @Id
    @Column(name = "user_id", length = 20)
    private String userId;

    @Column(name = "password", nullable = false, length = 100) // 비밀번호는 해싱하여 저장해야 하므로 길이를 충분히 확보
    private String password;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "address", nullable = false, length = 200)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "local", nullable = false, length = 50)
    private LocalType local;

    @Lob // CLOB 타입 매핑
    @Column(name = "notifications")
    private String notifications;


}