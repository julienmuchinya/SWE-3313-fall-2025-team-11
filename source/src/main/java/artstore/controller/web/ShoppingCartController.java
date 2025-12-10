package artstore.controller.web;

import artstore.controller.api.OrderController;
import artstore.entity.CartItem;
import artstore.entity.Order;
import artstore.entity.Payment;
import artstore.repository.CartItemRepository;
import artstore.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class ShoppingCartController {


    private final CartItemRepository cartItemRepository;

    public ShoppingCartController(CartItemRepository cartItemRepository) {

        this.cartItemRepository = cartItemRepository;
    }

    @GetMapping("/shopping-cart")
    public String shoppingCart(HttpSession session, Model model) {

        List<Long> productIds = SessionUtil.getCartProductIds(session);
        System.out.println("productIds = " + productIds);
        List<CartItem> items = cartItemRepository.findAll();
        System.out.println("items = " + items);

        BigDecimal total =  BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getArtPiece().getPrice());
        }

        model.addAttribute("cartItems", items);
        model.addAttribute("subtotal", total);
        model.addAttribute("count", items.size());

        return "shopping-cart";   // shopping-cart.html
    }

    @DeleteMapping("/cart/remove/{id}")
    @ResponseBody
    public Map<String, Object> removeFromCart(Model model, @PathVariable Long id, HttpSession session) {
        System.out.println("id = " + id);
        SessionUtil.removeProductFromCart(session, id);
        cartItemRepository.deleteById(id);

        Map<String, Object> result = new HashMap<>();
        List<Long> productIds = SessionUtil.getCartProductIds(session);
        List<CartItem> items = cartItemRepository.findAllById(productIds);

        BigDecimal total =  BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getArtPiece().getPrice());
        }

        model.addAttribute("cartItems", items);
        model.addAttribute("subtotal", total);



        result.put("success", true);
        result.put("subtotal", total);
        result.put("count", items.size());

        return result;
    }

    @PostMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        Order order = new Order();
        List<CartItem> cartItems = cartItemRepository.findAll();
        order.setItems(cartItems);
        Payment payment = new Payment();
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            total = total.add(item.getArtPiece().getPrice());
        }
        payment.setAmount(total.setScale(2, BigDecimal.ROUND_HALF_UP));
        order.setPayment(payment);
        return  "redirect:/pay-now/" + order.getId();
    }
}
