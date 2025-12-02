package artstore.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShoppingCartController {
    @GetMapping("shopping-cart")
    public String index() {
        return "shopping-cart";
    }
}
