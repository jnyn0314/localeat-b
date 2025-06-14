package javachip.entity.cart;

import jakarta.persistence.*;
import javachip.entity.user.User;

@Entity
@Table(name = "CONSUMER")
@PrimaryKeyJoinColumn(name = "ID")
public class Consumer extends User {

    private String birth;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CART_ID")
    private Cart cart;

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
    public String getId() {
        return super.getUserId(); // User 클래스에서 상속된 id
    }

}
