package artstore.controller.web;

import artstore.entity.ArtPiece;
import artstore.entity.CartItem;
import artstore.repository.ArtPieceRepository;
import artstore.repository.CartItemRepository;
import artstore.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ShoppingCartController {

    private final CartItemRepository cartItemRepository;
    private final ArtPieceRepository artPieceRepository;

    public ShoppingCartController(CartItemRepository cartItemRepository, ArtPieceRepository artPieceRepository) {
        this.cartItemRepository = cartItemRepository;
        this.artPieceRepository = artPieceRepository;
    }

    @GetMapping("/shopping-cart")
    public String shoppingCart(HttpSession session, Model model) {
        try {
            // Check if user is logged in
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId == null) {
                return "redirect:/sign-in?error=Please sign in to view your cart";
            }

            // Get product IDs from session cart
            List<Integer> productIds = SessionUtil.getCartProductIds(session);
            
            if (productIds.isEmpty()) {
                model.addAttribute("cartItems", List.of());
                model.addAttribute("subtotal", BigDecimal.ZERO);
                model.addAttribute("count", 0);
                return "shopping-cart";
            }

            // Load art pieces from database
            List<ArtPiece> artPieces = productIds.stream()
                    .map(id -> artPieceRepository.findById(id).orElse(null))
                    .filter(piece -> piece != null && piece.isActive() && piece.getOrderItem() == null)
                    .collect(Collectors.toList());

            BigDecimal total = artPieces.stream()
                    .map(ArtPiece::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            model.addAttribute("cartItems", artPieces);
            model.addAttribute("subtotal", total);
            model.addAttribute("count", artPieces.size());

            return "shopping-cart";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading cart: " + e.getMessage());
            return "shopping-cart";
        }
    }

    @PostMapping("/cart/add/{productId}")
    @ResponseBody
    public Map<String, Object> addToCart(@PathVariable Integer productId, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId == null) {
                result.put("success", false);
                result.put("message", "Please sign in to add items to cart");
                return result;
            }

            ArtPiece artPiece = artPieceRepository.findById(productId).orElse(null);
            if (artPiece == null || !artPiece.isActive() || artPiece.getOrderItem() != null) {
                result.put("success", false);
                result.put("message", "Item not available");
                return result;
            }

            SessionUtil.addProductToCart(session, productId);
            result.put("success", true);
            result.put("message", "Item added to cart");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error adding item: " + e.getMessage());
        }
        return result;
    }

    @DeleteMapping("/cart/remove/{productId}")
    @ResponseBody
    public Map<String, Object> removeFromCart(@PathVariable Integer productId,
                                              HttpSession session,
                                              Model model) {
        Map<String, Object> result = new HashMap<>();
        try {
            SessionUtil.removeProductFromCart(session, productId);

            // Recompute cart
            List<Integer> productIds = SessionUtil.getCartProductIds(session);
            List<ArtPiece> artPieces = productIds.stream()
                    .map(id -> artPieceRepository.findById(id).orElse(null))
                    .filter(piece -> piece != null && piece.isActive() && piece.getOrderItem() == null)
                    .collect(Collectors.toList());

            BigDecimal total = artPieces.stream()
                    .map(ArtPiece::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            result.put("success", true);
            result.put("subtotal", total);
            result.put("count", artPieces.size());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error removing item: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/shopping-cart/checkout")
    public String proceedToCheckout(HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId == null) {
                return "redirect:/sign-in?error=Please sign in to checkout";
            }

            List<Integer> productIds = SessionUtil.getCartProductIds(session);
            if (productIds.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Your cart is empty");
                return "redirect:/shopping-cart";
            }

            // Store productIds in session for pay-now page
            session.setAttribute("checkoutProductIds", productIds);
            return "redirect:/pay-now";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error proceeding to checkout: " + e.getMessage());
            return "redirect:/shopping-cart";
        }
    }
}
