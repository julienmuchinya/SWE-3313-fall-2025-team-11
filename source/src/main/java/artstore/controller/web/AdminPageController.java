package artstore.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController{
    @GetMapping("/admin-page")
    public String index() {
        return "admin-page";
    }
}
