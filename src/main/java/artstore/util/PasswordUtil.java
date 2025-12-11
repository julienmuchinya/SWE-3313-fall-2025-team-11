package artstore.util;

/**
 * Simple password helper. Right now it does plain-text compare
 * so it matches existing data. You can upgrade to hashing later.
 */
public class PasswordUtil {

    public static String hashPassword(String password) {
        // For now, just return as-is (no hashing).
        // If you add hashing later, update this method.
        return password;
    }

    public static boolean verifyPassword(String rawPassword, String storedHash) {
        if (rawPassword == null || storedHash == null) {
            return false;
        }
        // Plain text compare (matches hashPassword above)
        return rawPassword.equals(storedHash);
    }
}
