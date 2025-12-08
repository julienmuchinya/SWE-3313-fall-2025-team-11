package artstore.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Product")   // matches ERD "Product" table
public class ArtPiece {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    private String title;
    private String description;
    private BigDecimal price;
    private String imageUrl;

    // true = available to buy, false = sold/hidden
    private boolean isActive;

    private LocalDateTime createdAt;

    // 0..1 OrderItem (CartItem) for this art piece
    // This is the "Product 1 -> 0|1 OrderItem" relationship
    @OneToOne(mappedBy = "artPiece")
    private CartItem orderItem;

    public ArtPiece() {
    }

    public int getProductId() {
        return productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public CartItem getOrderItem() {
        return orderItem;
    }
    public void setOrderItem(CartItem orderItem) {
        this.orderItem = orderItem;
    }
}
