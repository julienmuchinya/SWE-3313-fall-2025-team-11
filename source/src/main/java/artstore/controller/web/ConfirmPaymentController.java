package artstore.controller.web;

import artstore.entity.Order;
import artstore.repository.OrderRepository;
import artstore.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Controller
public class ConfirmPaymentController {
    
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public ConfirmPaymentController(OrderRepository orderRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @GetMapping("/confirm-payment")
    public String confirmPayment(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId == null) {
                return "redirect:/sign-in?error=Please sign in";
            }

            Integer orderId = (Integer) session.getAttribute("pendingOrderId");
            if (orderId == null) {
                redirectAttributes.addFlashAttribute("error", "No pending order found");
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

            BigDecimal taxRate = new BigDecimal("0.06");
            BigDecimal salesTax = order.getPayment().getAmount().multiply(taxRate);
            salesTax = salesTax.setScale(2, RoundingMode.HALF_UP);
            model.addAttribute("salesTax", salesTax);

            return "confirm-payment";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error loading confirmation: " + e.getMessage());
            return "redirect:/shopping-cart";
        }
    }

    @PostMapping("/order/complete")
    public String completeOrder(HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId == null) {
                return "redirect:/sign-in?error=Please sign in";
            }

            Integer orderId = (Integer) session.getAttribute("pendingOrderId");
            if (orderId == null) {
                redirectAttributes.addFlashAttribute("error", "No pending order found");
                return "redirect:/shopping-cart";
            }

            // Finalize the order
            Order order = orderService.finalizeOrder(orderId);
            if (order == null) {
                redirectAttributes.addFlashAttribute("error", "Failed to complete order");
                return "redirect:/shopping-cart";
            }

            // Store order ID for receipt page
            session.setAttribute("completedOrderId", orderId);
            session.removeAttribute("pendingOrderId");

            return "redirect:/receipt";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error completing order: " + e.getMessage());
            return "redirect:/shopping-cart";
        }
    }
}
