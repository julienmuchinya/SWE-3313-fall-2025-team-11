package artstore.controller.web;

import artstore.entity.User;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignInController {

    private static final Logger log = LoggerFactory.getLogger(SignInController.class);

    private final artstore.controller.web.AuthController authController;

    public SignInController(artstore.controller.web.AuthController authController) {
        this.authController = authController;
    }

    // Show the sign-in page (GET /sign-in)
    @GetMapping("/sign-in")
    public String showSignInForm() {
        return "sign-in";
    }

    // Handle the sign-in form submission (POST /sign-in)
    @PostMapping("/sign-in")
    public String handleSignIn(
            @RequestParam("usernameEmail") String usernameEmail,
            @RequestParam("password") String password,
            @RequestParam(name = "loginType", required = false) String loginType,
            HttpSession session,
            Model model
    ) {
        try {
            // ---- Server-side validation ----

            if (loginType == null || loginType.isBlank()) {
                model.addAttribute("error", "Please select Regular User or Admin before signing in.");
                return "sign-in";
            }

            if (usernameEmail == null || usernameEmail.isBlank()) {
                model.addAttribute("error", "Username / Email is required.");
                return "sign-in";
            }

            if (password == null || password.isBlank()) {
                model.addAttribute("error", "Password is required.");
                return "sign-in";
            }

            // ---- Authenticate (username OR email) ----

            User user = authController.authenticate(usernameEmail, password);

            if (user == null) {
                model.addAttribute("error", "Invalid email/username or password.");
                return "sign-in";
            }

            // ---- Role check for admin login ----

            if ("ADMIN".equalsIgnoreCase(loginType)
                    && !"ADMIN".equalsIgnoreCase(user.getRole())) {

                model.addAttribute("error", "You do not have admin access.");
                return "sign-in";
            }

            // ---- Store user in session ----

            session.setAttribute("userId", user.getUserId());
            session.setAttribute("userEmail", user.getEmail());
            session.setAttribute("userRole", user.getRole());
            session.setAttribute("userFirstName", user.getFirstName());
            session.setAttribute("username", user.getUsername());

            // ---- Redirect based on loginType ----

            if ("ADMIN".equalsIgnoreCase(loginType)) {
                // valid admin login
                return "redirect:/admin";
            }

            // regular user login (or admin choosing regular mode)
            return "redirect:/";

        } catch (Exception ex) {
            // catch unexpected runtime errors to avoid Whitelabel page
            log.error("Error during sign-in", ex);
            model.addAttribute("error", "An unexpected error occurred while signing in. Please try again.");
            return "sign-in";
        }
    }
}
