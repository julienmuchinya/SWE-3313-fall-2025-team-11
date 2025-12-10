package artstore.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "OrderItem")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderItemId")   // MUST MATCH PK NAME IN DB
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OrderId")
    private Order order;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ProductId", unique = true, nullable = false)
    private ArtPiece artPiece;

    private Integer quantity;
    private BigDecimal unitPrice;

    public CartItem() {
    }


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }

    public ArtPiece getArtPiece() {
        return artPiece;
    }
    public void setArtPiece(ArtPiece artPiece) {
        this.artPiece = artPiece;
    }

    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
