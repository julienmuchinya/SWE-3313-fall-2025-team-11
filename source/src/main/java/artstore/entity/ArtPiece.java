package artstore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtPiece {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String title;
    private String artist;
    private String description;
    private BigDecimal price;
    private String imageUrl;

    private boolean isActive;
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "artPiece")
    private CartItem orderItem;

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }
}
