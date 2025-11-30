package artstore.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public class AuthController {

    @GetMapping
    public String index() {
        return "auth";
    }
}
