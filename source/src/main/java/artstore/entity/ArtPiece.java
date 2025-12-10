package artstore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ArtPiece")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtPiece {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    // ✅ artist name added
    @Column(nullable = false)
    private String artistName;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    private String imageUrl;

    // ✅ this matches "isActive" in repository method name
    @Column(nullable = false)
    private boolean isActive = true;

    // ✅ back-reference so "OrderItemIsNull" in repo works
    @OneToOne(mappedBy = "artPiece")
    private CartItem orderItem;
}
