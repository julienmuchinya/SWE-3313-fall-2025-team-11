package artstore.util;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

public class SessionUtil {

    private static final String CART_KEY = "CART_PRODUCT_IDS";

    // ... your existing methods stay

    // Get or create cart list (productId list)
    public static List<Integer> getCartProductIds(HttpSession session) {
        @SuppressWarnings("unchecked")
        List<Integer> cart = (List<Integer>) session.getAttribute(CART_KEY);
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute(CART_KEY, cart);
        }
        return cart;
    }

    // Add product to cart (unique items only)
    public static void addProductToCart(HttpSession session, Integer productId) {
        List<Integer> cart = getCartProductIds(session);
        if (!cart.contains(productId)) {  // don't add twice
            cart.add(productId);
        }
    }

    // Remove product from cart
    public static void removeProductFromCart(HttpSession session, Integer productId) {
        List<Integer> cart = getCartProductIds(session);
        cart.removeIf(id -> id.equals(productId));
    }
}
