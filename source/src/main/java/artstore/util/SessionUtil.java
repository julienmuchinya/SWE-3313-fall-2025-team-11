package artstore.util;

import jakarta.servlet.http.HttpSession;

public class SessionUtil {

    public static boolean isLoggedIn(HttpSession session) {
        return session != null && session.getAttribute("userId") != null;
    }

    public static boolean isAdmin(HttpSession session) {
        if (session == null) return false;
        Object role = session.getAttribute("userRole");
        return role != null && "ADMIN".equalsIgnoreCase(role.toString());
    }

    public static Long getCurrentUserId(HttpSession session) {
        if (session == null) return null;
        Object id = session.getAttribute("userId");
        if (id instanceof Long) {
            return (Long) id;
        }
        if (id instanceof Integer) {
            return ((Integer) id).longValue();
        }
        return null;
    }
}
