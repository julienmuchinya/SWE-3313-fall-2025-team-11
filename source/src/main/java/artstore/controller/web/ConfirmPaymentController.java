package artstore.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConfirmPaymentController {
    @GetMapping("/confirm-payment")
    public String confirmPayment() {
        return "confrim-payment";
    }
}
