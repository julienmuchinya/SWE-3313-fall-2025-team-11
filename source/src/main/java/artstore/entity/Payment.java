package artstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Payment")

public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long paymentId;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OrderId", nullable = false, unique = true)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PaymentMethodId", nullable = false)
    private PaymentMethod paymentMethod;

    private BigDecimal amount;
    private String status;

    @Column(columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime paidAt;

    @PrePersist
    protected void prePersist() {
        if (this.paidAt == null) {
            this.paidAt = LocalDateTime.now();
        }
    }
}