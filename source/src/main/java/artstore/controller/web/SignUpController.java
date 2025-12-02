package artstore.controller.web;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignUpController {
    @GetMapping("/sign-up")
    public String signUp() {
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String getSignUp(@RequestParam String username,
                            @RequestParam String email,
                            @RequestParam String confirmEmail,
                            @RequestParam String password,
                            @RequestParam String confirmPassword,
                            Model model) {
        System.out.println(username);
        System.out.println(password);

        if (!email.equals(confirmEmail)) {
            model.addAttribute("error", "Emails do not match, try again");
            return "sign-up";
        }  else if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match, try again");
            return "sign-up";
        } else if (username.equals("1")) {
            model.addAttribute("error", "Username Taken, try Sign In");
            return "sign-up";
        }  else if (email.equals("1234@gmail.com")) {
            model.addAttribute("error", "Email Taken, try Sign In");
            return "sign-up";
        } else if (password.length() < 3) {
            model.addAttribute("error", "Password too short, try again");
            return "sign-up";
        }

        return "redirect:/inventory";
    }
}
