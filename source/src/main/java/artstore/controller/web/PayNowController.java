package artstore.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PayNowController {
    @GetMapping("/pay-now")
    public String index() {
        return "pay-now";
    }
}
