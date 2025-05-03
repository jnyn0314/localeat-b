package javachip.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "\"Seller\"")
@PrimaryKeyJoinColumn(name = "user_id") // 부모 테이블 User의 PK(user_id)와 조인됨을 명시
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
// @DiscriminatorValue("1") // 만약 User 엔티티에 @DiscriminatorColumn을 사용했다면, 해당 값 지정
public class Seller extends User {

    @Column(name = "business_id", nullable = false, length = 30)
    private String businessId; // DDL: VARCHAR2(30)

    // Product와의 관계 설정 (Product 테이블에 seller_id FK가 있으므로 Seller는 주인이 아님)
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>(); // Product 엔티티가 정의되어 있어야 함

    // Lombok @SuperBuilder를 사용하면 부모 필드를 포함하는 빌더 생성


    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }


    //이거
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}