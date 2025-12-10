package artstore.controller.web;

import artstore.entity.Order;
import artstore.repository.OrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Controller
public class ReceiptController {

    private final OrderRepository orderRepository;

    public ReceiptController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("receipt/{id}")
    public String receipt(@PathVariable Long id, Model model) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        Order order;

        if (orderOptional.isPresent()) {
            order = orderOptional.get();
            model.addAttribute("order", order);

            BigDecimal taxRate = new BigDecimal("0.06");
            BigDecimal salesTax = order.getPayment().getAmount().multiply(taxRate);
            salesTax = salesTax.setScale(2, RoundingMode.HALF_UP);
            model.addAttribute("salesTax", salesTax);

            return "receipt";
        }

        return "index";
    }
}
