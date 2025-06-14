package javachip.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "SELLER")
@PrimaryKeyJoinColumn(name = "ID")
public class Seller extends User {

    @Column(name = "BUSINESS_ID", nullable = false)
    private String businessId;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
}
