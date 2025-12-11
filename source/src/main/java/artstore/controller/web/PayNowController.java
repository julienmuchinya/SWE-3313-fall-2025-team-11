package artstore.controller.web;

import artstore.controller.dto.PaymentForm;
import artstore.entity.ArtPiece;
import artstore.entity.Order;
import artstore.entity.Payment;
import artstore.entity.PaymentMethod;
import artstore.model.PayNowForm;
import artstore.repository.ArtPieceRepository;
import artstore.repository.OrderRepository;
import artstore.repository.PaymentMethodRepository;
import artstore.repository.PaymentRepository;
import artstore.service.OrderService;
import artstore.util.PriceCalculator;
import artstore.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class PayNowController {

    private final OrderRepository orderRepository;
    private final ArtPieceRepository artPieceRepository;
    private final OrderService orderService;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentRepository paymentRepository;

    public PayNowController(
            OrderRepository orderRepository,
            ArtPieceRepository artPieceRepository,
            OrderService orderService,
            PaymentMethodRepository paymentMethodRepository,
            PaymentRepository paymentRepository
    ) {
        this.orderRepository = orderRepository;
        this.artPieceRepository = artPieceRepository;
        this.orderService = orderService;
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentRepository = paymentRepository;
    }

    @GetMapping("/pay-now")
    public String showPayNowForm(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId == null) {
                return "redirect:/sign-in?error=Please sign in to checkout";
            }

            // Get product IDs from session
            @SuppressWarnings("unchecked")
            List<Integer> productIds = (List<Integer>) session.getAttribute("checkoutProductIds");
            if (productIds == null || productIds.isEmpty()) {
                productIds = SessionUtil.getCartProductIds(session);
            }

            if (productIds.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Your cart is empty");
                return "redirect:/shopping-cart";
            }

            // Load art pieces
            List<ArtPiece> artPieces = new ArrayList<>();
            for (Integer id : productIds) {
                Optional<ArtPiece> opt = artPieceRepository.findById(id);
                if (opt.isPresent() && opt.get().isActive() && opt.get().getOrderItem() == null) {
                    artPieces.add(opt.get());
                }
            }

            if (artPieces.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "No valid items in cart");
                return "redirect:/shopping-cart";
            }

            // Calculate totals
            BigDecimal subtotal = artPieces.stream()
                    .map(ArtPiece::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal shippingFee = new BigDecimal("19.00"); // Default
            BigDecimal tax = subtotal.multiply(new BigDecimal("0.06"))
                    .setScale(2, RoundingMode.HALF_UP);
            BigDecimal orderTotal = subtotal.add(shippingFee).add(tax)
                    .setScale(2, RoundingMode.HALF_UP);

            // Create a temporary order object for display
            Order tempOrder = new Order();
            tempOrder.setItems(new ArrayList<>());
            for (ArtPiece piece : artPieces) {
                artstore.entity.CartItem item = new artstore.entity.CartItem();
                item.setArtPiece(piece);
                tempOrder.addItem(item);
            }

            model.addAttribute("order", tempOrder);
            model.addAttribute("subtotal", subtotal);
            model.addAttribute("shippingFee", shippingFee);
            model.addAttribute("tax", tax);
            model.addAttribute("orderTotal", orderTotal);
            model.addAttribute("listShippingMethods", Arrays.asList("GROUND", "THREE_DAY", "OVERNIGHT"));
            model.addAttribute("payNowForm", new PayNowForm());

            return "pay-now";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error loading checkout: " + e.getMessage());
            return "redirect:/shopping-cart";
        }
    }

    @PostMapping("/pay-now/submit")
    public String submitPayment(
            @ModelAttribute PayNowForm form,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId == null) {
                return "redirect:/sign-in?error=Please sign in to checkout";
            }

            // Validate form
            if (form.getFullName() == null || form.getFullName().isBlank() ||
                form.getAddress() == null || form.getAddress().isBlank() ||
                form.getCreditCardNumber() == null || form.getCreditCardNumber().isBlank() ||
                form.getShippingMethod() == null || form.getShippingMethod().isBlank()) {
                model.addAttribute("error", "Please fill in all required fields");
                return showPayNowForm(session, model, redirectAttributes);
            }

            // Get product IDs
            @SuppressWarnings("unchecked")
            List<Integer> productIds = (List<Integer>) session.getAttribute("checkoutProductIds");
            if (productIds == null || productIds.isEmpty()) {
                productIds = SessionUtil.getCartProductIds(session);
            }

            if (productIds.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Your cart is empty");
                return "redirect:/shopping-cart";
            }

            // Create order
            Order order = orderService.createOrder(userId, productIds);
            if (order == null) {
                redirectAttributes.addFlashAttribute("error", "Failed to create order");
                return "redirect:/shopping-cart";
            }

            // Calculate totals
            BigDecimal subtotal = PriceCalculator.calculateTotal(order);
            String shippingMethod = form.getShippingMethod().toUpperCase();
            BigDecimal shippingFee = switch (shippingMethod) {
                case "OVERNIGHT" -> new BigDecimal("29.00");
                case "THREE_DAY" -> new BigDecimal("19.00");
                default -> BigDecimal.ZERO;
            };
            BigDecimal tax = subtotal.multiply(new BigDecimal("0.06"))
                    .setScale(2, RoundingMode.HALF_UP);
            BigDecimal orderTotal = subtotal.add(shippingFee).add(tax)
                    .setScale(2, RoundingMode.HALF_UP);

            order.setTotalAmount(orderTotal);
            order.setStatus("PENDING");
            order = orderRepository.save(order);

            // Create payment method
            PaymentMethod paymentMethod = new PaymentMethod();
            paymentMethod.setUser(order.getUser());
            paymentMethod.setProvider("CREDIT_CARD");
            String cardNumber = form.getCreditCardNumber();
            String last4 = cardNumber.length() >= 4 
                ? cardNumber.substring(cardNumber.length() - 4) 
                : "0000";
            paymentMethod.setLast4(last4);
            paymentMethod.setCreatedAt(LocalDateTime.now());
            paymentMethod = paymentMethodRepository.save(paymentMethod);

            // Create payment
            Payment payment = new Payment();
            payment.setOrder(order);
            payment.setPaymentMethod(paymentMethod);
            payment.setAmount(orderTotal);
            payment.setShippingFee(shippingFee);
            payment.setStatus("PENDING");
            payment.setPaidAt(LocalDateTime.now());
            payment = paymentRepository.save(payment);

            // Clear cart
            SessionUtil.getCartProductIds(session).clear();
            session.removeAttribute("checkoutProductIds");

            // Store order ID in session for confirmation page
            session.setAttribute("pendingOrderId", order.getOrderId());

            return "redirect:/confirm-payment";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error processing payment: " + e.getMessage());
            return "redirect:/pay-now";
        }
    }
}
