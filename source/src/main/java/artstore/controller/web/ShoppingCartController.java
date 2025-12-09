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
        model.addAttribute("subtotal", total);

        return "shopping-cart";   // shopping-cart.html
    }

    @DeleteMapping("/cart/remove/{id}")
    @ResponseBody
    public String removeFromCart(Model model, @PathVariable int id, HttpSession session) {
        SessionUtil.removeProductFromCart(session, id);



        return "success";
    }
}
