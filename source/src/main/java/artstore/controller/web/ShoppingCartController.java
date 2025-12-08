package artstore.controller.web;

import artstore.entity.ArtPiece;
import artstore.repository.ArtPieceRepository;
import artstore.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

    private final ArtPieceRepository artPieceRepository;

    public ShoppingCartController(ArtPieceRepository artPieceRepository) {
        this.artPieceRepository = artPieceRepository;
    }

    // VIEW CART
    @GetMapping
    public String shoppingCart(HttpSession session, Model model) {
        List<Integer> productIds = SessionUtil.getCartProductIds(session);

        // load all ArtPieces in the cart
        List<ArtPiece> items = artPieceRepository.findAllById(productIds);

        // calculate total here (simple sum)
        BigDecimal total = items.stream()
                .map(ArtPiece::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("items", items);
        model.addAttribute("total", total);

        return "shopping-cart";   // shopping-cart.html
    }

    // ADD ITEM TO CART
    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable int productId, HttpSession session) {
        // only add if active + not sold
        artPieceRepository.findByProductIdAndIsActiveTrueAndOrderItemIsNull(productId)
                .ifPresent(p -> SessionUtil.addProductToCart(session, productId));

        return "redirect:/shopping-cart";
    }

    // REMOVE ITEM FROM CART
    @PostMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable int productId, HttpSession session) {
        SessionUtil.removeProductFromCart(session, productId);
        return "redirect:/shopping-cart";
    }

    // CLEAR CART
    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        SessionUtil.clearCart(session);
        return "redirect:/shopping-cart";
    }
}
