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
@Table(name = "orders") 
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    // ============================
    // USER WHO PLACED THE ORDER
    // ============================
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") 
    private User user;

    // ============================
    // ORDER META
    // ============================
    @Column(nullable = false)
    private LocalDateTime createdAt; 

    
    @Column(nullable = false)
    private BigDecimal totalAmount;

    // ============================
    // PAYMENT INFO (SIMPLE MODEL) 
    // ============================
    @Column(nullable = false)
    private String paymentMethod;   // "CARD", "CASH", etc.

    @Column(nullable = false)
    private String paymentStatus;   // "PENDING", "PAID"

    private LocalDateTime paymentDate;

    @Column(nullable = false)
    private String status;          // "NEW", "COMPLETED"

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;

    // ============================
    // ORDER ITEMS (CartItem)
    // ============================
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();


    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    // ============================
    // GETTERS + SETTERS 
    // ============================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public void addItem(CartItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void removeItem(CartItem item) {
        items.remove(item);
        item.setOrder(null);
    }
}
