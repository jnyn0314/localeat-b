/*
    파일명 : User.java
    파일설명 : User 테이블 엔티티
    작성자 : 김소망
    기간 : 2025-05.03.
*/
package javachip.entity;


import jakarta.persistence.*;

import lombok.Getter;
import lombok.Builder;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "USERS")
public abstract class User {
    @Id
    @Column(name = "ID")
    private String userId;

    private String password;
    private String name;
    private String phone;
    private String email;
    private String address;
    @Enumerated(EnumType.STRING) // Enum 값을 문자열로 저장
    private LocalType local;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Lob
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

    public LocalType getLocal() {
        return local;
    }

    public void setLocal(LocalType local) {
        this.local = local;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
