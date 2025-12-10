package artstore.controller.web;

import artstore.entity.User;
import artstore.repository.UserRepository;
import artstore.util.PasswordUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/sign-in")
public class SignInController {

    private final UserRepository userRepository;

    public SignInController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String showSignInForm(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "sign-in";
    }

    @PostMapping
    public String handleSignIn(
            @RequestParam("usernameOrEmail") String usernameOrEmail,
            @RequestParam("password") String password,
            @RequestParam(value = "loginType", required = false) String loginType,
            HttpSession session,
            Model model
    ) {
        try {
            // Validate loginType was selected
            if (loginType == null || loginType.isBlank()) {
                model.addAttribute("error", "Please select Regular User or Admin before signing in.");
                return "sign-in";
            }

            if (usernameOrEmail == null || usernameOrEmail.isBlank()
                    || password == null || password.isBlank()) {
                model.addAttribute("error", "Username/email and password are required.");
                return "sign-in";
            }

            // Try email first
            Optional<User> userOpt = userRepository.findByEmail(usernameOrEmail);
            // If not found by email, try username
            if (userOpt.isEmpty()) {
                userOpt = userRepository.findByUsername(usernameOrEmail);
            }

            if (userOpt.isEmpty()) {
                model.addAttribute("error", "Invalid username/email or password.");
                return "sign-in";
            }

            User user = userOpt.get();

            // Verify the selected role matches the user's role
            String expectedRole = "ADMIN".equalsIgnoreCase(loginType) ? "ADMIN" : "USER";
            if (!expectedRole.equalsIgnoreCase(user.getRole())) {
                model.addAttribute("error", "Invalid role selection. Please select the correct role.");
                return "sign-in";
            }

            // Check password using PasswordUtil
            boolean valid = PasswordUtil.verifyPassword(password, user.getPasswordHash());
            if (!valid) {
                model.addAttribute("error", "Invalid username/email or password.");
                return "sign-in";
            }

            // Store info in session
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("userEmail", user.getEmail());
            session.setAttribute("userRole", user.getRole());
            session.setAttribute("userFirstName", user.getFirstName());
            session.setAttribute("username", user.getUsername());

            // Redirect based on role
            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                return "redirect:/admin-page";
            } else {
                return "redirect:/inventory";
            }
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred during sign in. Please try again.");
            return "sign-in";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
