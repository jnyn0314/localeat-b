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

@Entity
@Table(name = "\"User\"") // Oracle에서 대소문자 구분 테이블 이름 사용 시 따옴표 필요
@Inheritance(strategy = InheritanceType.JOINED) // 조인 전략 사용
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

    @Column(name = "local", nullable = false, length = 50) // Enum으로 관리하는 것을 고려해보세요
    private String local;

    @Column(name = "role", nullable = false) // DDL: NUMBER(1) DEFAULT 0 NOT NULL
    private Integer role; // 0: Consumer, 1: Seller (또는 다른 규칙) -> Enum 타입 사용 권장

    @Lob // CLOB 타입 매핑
    @Column(name = "notifications")
    private String notifications;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getNotifications() {
        return notifications;
    }

    public void setNotifications(String notifications) {
        this.notifications = notifications;
    }
}