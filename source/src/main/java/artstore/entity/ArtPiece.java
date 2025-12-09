package artstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
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

    public ArtPiece() {}

    @PrePersist
    void PrePersist() {
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }
}
