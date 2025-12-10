package artstore.controller.web;

import artstore.entity.Order;
import artstore.repository.OrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class PayNowController {
    private final OrderRepository orderRepository;

    public PayNowController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    @GetMapping("pay-now/{id}")
    public String receipt(@PathVariable Long id, Model model) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        Order order;

        if (orderOptional.isPresent()) {
            order = orderOptional.get();
            model.addAttribute("order", order);
            return "receipt";
        }

        return "index";
    }
}
