package artstore.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Orders") // avoid SQL keyword "order"
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;     // 🔁 was Long, now Integer

    // ============================
    // USER WHO PLACED THE ORDER
    // ============================
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    // ============================
    // ORDER META
    // ============================
    @Column(nullable = false)
    private LocalDateTime orderDate;

    private String status;   // "PENDING", "PAID"

    private BigDecimal totalAmount;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;

    // ============================
    // ORDER ITEMS (CartItem)
    // ============================
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.orderDate == null) {
            this.orderDate = LocalDateTime.now();
        }
    }

    public void addItem(CartItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void removeItem(CartItem item) {
        items.remove(item);
        item.setOrder(null);
    }
}
