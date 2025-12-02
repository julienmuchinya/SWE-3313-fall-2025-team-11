package artstore.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignInController {
    @GetMapping("/sign-in")
    public String signIn() {
        return "sign-in";
    }

    @PostMapping("/sign-in")
    public String getSignIn(@RequestParam String usernameEmail, @RequestParam String password, Model model) {
        System.out.println(usernameEmail);
        System.out.println(password);

        if (!usernameEmail.equals("1") || !password.equals("2")) {
            model.addAttribute("error", "Invalid Username/Email or Password");
            return "sign-in";
        }

        return "redirect:/inventory";
    }
}
