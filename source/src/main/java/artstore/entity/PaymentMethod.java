package artstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "PaymentMethod")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long paymentMethodId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId", nullable = false)
    private User user;

    private String provider;
    private String last4;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "paymentMethod")
    private List<Payment> payments;

    @PrePersist
    protected void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}

