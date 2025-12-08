package artstore.controller.web;

import artstore.repository.ArtPieceRepository;
import artstore.repository.OrderRepository;
import artstore.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {

    private final UserRepository userRepository;
    private final ArtPieceRepository artPieceRepository;
    private final OrderRepository orderRepository;

    // Inject repositories so admin can see users, art, orders
    public AdminPageController(UserRepository userRepository,
                               ArtPieceRepository artPieceRepository,
                               OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.artPieceRepository = artPieceRepository;
        this.orderRepository = orderRepository;
    }

    // GET /admin
    @GetMapping("/admin")
    public String showAdminPage(HttpSession session, Model model) {
        // 1) Check if someone is logged in
        Object userId = session.getAttribute("userId");
        Object role = session.getAttribute("userRole");

        if (userId == null || role == null) {
            // Not logged in at all → send to sign-in
            return "redirect:/sign-in";
        }

        // 2) Check if user is ADMIN
        String roleStr = role.toString();
        if (!"ADMIN".equalsIgnoreCase(roleStr)) {
            // Logged in but not admin → send them to home (or 403 page)
            return "redirect:/";
        }

        // 3) At this point, user is ADMIN. Load data for dashboard.

        // Example: load all art pieces
        model.addAttribute("artPieces", artPieceRepository.findAll());

        // Example: load all orders
        model.addAttribute("orders", orderRepository.findAll());

        // Example: load all users
        model.addAttribute("users", userRepository.findAll());

        // 4) Return the admin template (admin.html)
        return "admin";
    }
}
