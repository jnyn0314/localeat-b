package javachip.entity;

import jakarta.persistence.*;

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
}
