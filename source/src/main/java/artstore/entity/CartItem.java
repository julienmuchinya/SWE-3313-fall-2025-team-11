package artstore.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "OrderItem")   
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderItemId")
    private Long id;

    // many items in one order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId")
    private Order order;

    // 1 Product -> 0 or 1 OrderItem
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "productId", unique = true)
    private ArtPiece artPiece;

    @Transient
    private Integer quantity;

    @Transient
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
