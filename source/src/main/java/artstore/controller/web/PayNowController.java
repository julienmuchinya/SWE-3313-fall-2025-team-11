package artstore.controller.web;

import artstore.entity.Order;
import artstore.model.PayNowForm;
import artstore.repository.OrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class PayNowController {
    private final OrderRepository orderRepository;

    public PayNowController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    @GetMapping("/pay-now/{id}")
    public String payNow(Model model, @PathVariable long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        Order order;

        if (orderOptional.isPresent()) {
            order = orderOptional.get();
            model.addAttribute("order", order);
            BigDecimal taxRate = new BigDecimal("0.06");
            BigDecimal salesTax = order.getPayment().getAmount().multiply(taxRate);
            salesTax = salesTax.setScale(2, RoundingMode.HALF_UP);
            model.addAttribute("salesTax", salesTax);



            PayNowForm payNowForm = new PayNowForm();
            model.addAttribute("payNowForm", payNowForm);

            List<String> listShippingMethods = Arrays.asList("Overnight", "3-Day", "Ground");
            model.addAttribute("listShippingMethods", listShippingMethods);
            return "pay-now";
        }


        return "index";
    }
}
