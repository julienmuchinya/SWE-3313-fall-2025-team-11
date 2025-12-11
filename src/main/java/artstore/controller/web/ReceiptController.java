package artstore.controller.web;

import artstore.entity.Order;
import artstore.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class ReceiptController {
    
    private final OrderRepository orderRepository;

    public ReceiptController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/receipt")
    public String receipt(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId == null) {
                return "redirect:/sign-in?error=Please sign in";
            }

            Integer orderId = (Integer) session.getAttribute("completedOrderId");
            if (orderId == null) {
                redirectAttributes.addFlashAttribute("error", "No order found");
                return "redirect:/shopping-cart";
            }

            Optional<Order> orderOpt = orderRepository.findById(orderId);
            if (orderOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Order not found");
                return "redirect:/shopping-cart";
            }

            Order order = orderOpt.get();
            
            // Verify order belongs to user
            if (!order.getUser().getUserId().equals(userId)) {
                redirectAttributes.addFlashAttribute("error", "Unauthorized access");
                return "redirect:/shopping-cart";
            }

            model.addAttribute("order", order);
            
            // Clear the completed order ID from session
            session.removeAttribute("completedOrderId");
            
            return "receipt";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error loading receipt: " + e.getMessage());
            return "redirect:/shopping-cart";
        }
    }
}
