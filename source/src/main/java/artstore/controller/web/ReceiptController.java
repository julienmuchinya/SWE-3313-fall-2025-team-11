package artstore.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReceiptController {
    @GetMapping("receipt")
    public String index() {
        return "receipt";
    }
}
