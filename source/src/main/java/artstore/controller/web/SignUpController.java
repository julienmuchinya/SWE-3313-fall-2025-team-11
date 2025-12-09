package artstore.controller.web;

import artstore.entity.User;
import artstore.repository.UserRepository;
import artstore.util.PasswordUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class SignUpController {

    private final UserRepository userRepository;

    public SignUpController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Show the sign-up page (GET /sign-up)
    @GetMapping("/sign-up")
    public String showSignUpForm() {
        return "sign-up";  // sign-up.html
    }

    // Handle the sign-up form submission (POST /sign-up)
    @PostMapping("/sign-up")
    public String handleSignUp(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("confirmEmail") String confirmEmail,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model
    ) {
        // 1) Emails must match
        if (!email.equals(confirmEmail)) {
            model.addAttribute("error", "Email and Confirm Email do not match.");
            return "sign-up";
        }

        // 2) Passwords must match
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Password and Confirm Password do not match.");
            return "sign-up";
        }

        // 3) Username must be unique
        Optional<User> existingByUsername = userRepository.findByUsername(username);
        if (existingByUsername.isPresent()) {
            model.addAttribute("error", "Username is already taken.");
            return "sign-up";
        }

        // 4) Email must be unique
        Optional<User> existingByEmail = userRepository.findByEmail(email);
        if (existingByEmail.isPresent()) {
            model.addAttribute("error", "Email is already registered.");
            return "sign-up";
        }

        // 5) Hash the password before storing it
        String hashedPassword = PasswordUtil.hashPassword(password);

        // 6) Create a new User object with default role USER
        User user = new User();
        user.setUsername(username);
        user.setFirstName(username);          // reuse username as firstName/display name for now
        user.setLastName("");                 // no last name in your form
        user.setEmail(email);
        user.setPasswordHash(hashedPassword);
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());

        // 7) Save the user in the database
        userRepository.save(user);

        // 8) Redirect to login page after successful sign-up
        return "redirect:/sign-in";
        // or: return "redirect:/inventory"; if you want to send them to the art listing instead
    }
}
