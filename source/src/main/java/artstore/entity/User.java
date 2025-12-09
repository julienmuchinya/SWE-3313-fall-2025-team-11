package artstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")  // matches your DB table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    // NEW: username column
    @Column(nullable = false, unique = true)
    private String username;

    private String firstName;
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String role;  // USER or ADMIN

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Lombok gives default no-args constructor automatically

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.role == null) {
            this.role = "USER";   // default role for new accounts
        }
    }
}

