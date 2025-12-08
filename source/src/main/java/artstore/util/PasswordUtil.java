package artstore.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// Utility class for password hashing and verification
public class PasswordUtil {

    // Reuse a single encoder instance
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Hash a plain-text password before saving to DB
    public static String hashPassword(String plainPassword) {
        return encoder.encode(plainPassword);
    }

    // Check if a plain-text password matches a stored hash
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return encoder.matches(plainPassword, hashedPassword);
    }
}
