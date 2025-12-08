package artstore.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "OrderItem")   // matches ERD "OrderItem" table
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    // many items in one order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId")
    private Order order;

    // 1 Product -> 0 or 1 OrderItem
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "productId", unique = true)
    private ArtPiece artPiece;

    // kept for compatibility – but in your logic you always use quantity = 1
    private Integer quantity;
    private BigDecimal unitPrice;

    public CartItem() {
    }

    public Long getOrderItemId() {
        return orderItemId;
    }
    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
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
