package artstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {
    @GetMapping("/index")
    public String index() {
        return "index";
    }
    @GetMapping("/inventory")
    public String inventory() {
        return "inventory";
    }
    @GetMapping("/sign-in")
    public String signIn() {
        return "sign-in";
    }
    @GetMapping("/sign-up")
    public String signUp() {
        return "sign-up";
    }
    @GetMapping("/shopping-cart")
    public String shoppingCart() {
        return "shopping-cart";
    }
    @GetMapping("/pay-now")
    public String payNow() {
        return "pay-now";
    }
    @GetMapping("/confirm-payment")
    public String confirmPayment() {
        return "confirm-payment";
    }
    @GetMapping("/receipt")
    public String receipt() {
        return "receipt";
    }
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

}
