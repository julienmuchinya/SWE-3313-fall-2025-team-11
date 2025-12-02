package artstore.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InventoryController {
    @GetMapping("/inventory")
    public String index() {
        return "inventory";
    }
}
