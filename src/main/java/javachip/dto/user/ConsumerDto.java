package javachip.dto.user;

import javachip.entity.LocalType;

public class ConsumerDto {

    private String userId;
    private String name;
    private String phone;
    private String email;
    private String address;
    private LocalType local;
    private String birth;

    // 기본 생성자
    public ConsumerDto() {
    }

    // 생성자 오버로드
    public ConsumerDto(String userId, String name, String phone, String email,
                       String address, LocalType local, String birth) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.local = local;
        this.birth = birth;
    }

    // Getters & Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }
}

