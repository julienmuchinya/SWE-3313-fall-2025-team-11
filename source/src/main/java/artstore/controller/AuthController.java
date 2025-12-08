package artstore.controller.web;

import artstore.entity.User;
import artstore.repository.UserRepository;
import artstore.util.PasswordUtil;
import org.springframework.stereotype.Component;

import java.util.Optional;

// Handles the core authentication logic (not a web endpoint)
@Component
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Try to authenticate with username OR email + plain-text password.
     * Returns the User if login is successful, or null if it fails.
     */
    public User authenticate(String usernameOrEmail, String plainPassword) {
        Optional<User> optionalUser = userRepository.findByEmail(usernameOrEmail);

        // If not found by email, try username
        if (optionalUser.isEmpty()) {
            optionalUser = userRepository.findByUsername(usernameOrEmail);
        }

        if (optionalUser.isEmpty()) {
            return null; // no user with that email/username
        }

        User user = optionalUser.get();

        // Verify plain password against stored hash
        boolean valid = PasswordUtil.verifyPassword(plainPassword, user.getPasswordHash());

        if (!valid) {
            return null; // wrong password
        }

        return user; // success
    }
}
