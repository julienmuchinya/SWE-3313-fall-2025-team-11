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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/shopping-cart")
public class ShoppingCartController {


    private final CartItemRepository cartItemRepository;

    public ShoppingCartController(CartItemRepository cartItemRepository) {

        this.cartItemRepository = cartItemRepository;
    }

    @GetMapping
    public String shoppingCart(HttpSession session, Model model) {

        List<Long> productIds = SessionUtil.getCartProductIds(session);
        System.out.println("productIds = " + productIds);
        List<CartItem> items = cartItemRepository.findAllbyOrderItemId(productIds);
        System.out.println("items = " + items);

        BigDecimal total =  BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getArtPiece().getPrice());
        }

        model.addAttribute("cartItems", items);
        model.addAttribute("subtotal", total);

        return "shopping-cart";   // shopping-cart.html
    }

    @DeleteMapping("/cart/remove/{id}")
    @ResponseBody
    public Map<String, Object> removeFromCart(Model model, @PathVariable Long id, HttpSession session) {
        SessionUtil.removeProductFromCart(session, id);

        Map<String, Object> result = new HashMap<>();
        List<Long> productIds = SessionUtil.getCartProductIds(session);
        List<CartItem> items = cartItemRepository.findAllById(productIds);

        BigDecimal total =  BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getArtPiece().getPrice());
        }

        result.put("success", true);
        result.put("total", total);
        result.put("count", items.size());

        return result;
    }
}
