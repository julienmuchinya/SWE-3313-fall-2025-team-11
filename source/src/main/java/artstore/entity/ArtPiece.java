package artstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
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

    // Product 1 -> 0|1 OrderItem
    @OneToOne(mappedBy = "artPiece")
    private CartItem orderItem;

    // remove manual constructor — Lombok gives a default no-args constructor automatically

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.isActive = true;   // new art is always active by default
    }
}
