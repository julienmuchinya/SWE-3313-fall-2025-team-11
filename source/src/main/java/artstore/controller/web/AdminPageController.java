package artstore.controller.web;

import artstore.entity.ArtPiece;
import artstore.entity.User;
import artstore.model.InventoryItemForm;
import artstore.model.PromoteAdminForm;
import artstore.repository.ArtPieceRepository;
import artstore.repository.OrderRepository;
import artstore.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Controller
public class AdminPageController {

    private final ArtPieceRepository artPieceRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public AdminPageController(ArtPieceRepository artPieceRepository,
                               UserRepository userRepository,
                               OrderRepository orderRepository) {
        this.artPieceRepository = artPieceRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/admin-page")
    public String adminPage(Model model) {
        model.addAttribute("promoteAdminForm", new PromoteAdminForm());
        model.addAttribute("inventoryItemForm", new InventoryItemForm());
        model.addAttribute("orderRepository", orderRepository);
        return "admin-page";
    }

    // ===============================
    // PROMOTE USER TO ADMIN
    // ===============================
    @PostMapping("/promote/admin")
    @ResponseBody
    public String promoteAdmin(@ModelAttribute PromoteAdminForm promoteAdminForm) {

        if (promoteAdminForm.getUsername() == null || promoteAdminForm.getUsername().trim().isEmpty()) {
            return "Username is required";
        }

        if (!promoteAdminForm.getUsername().equals(promoteAdminForm.getConfirmUsername())) {
            return "Username and/or Confirm Username is incorrect";
        }

        Optional<User> userOpt = userRepository.findByUsername(promoteAdminForm.getUsername());
        if (userOpt.isEmpty()) {
            return promoteAdminForm.getUsername() + " does not match any known Users";
        }

        User user = userOpt.get();
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            return promoteAdminForm.getUsername() + " is already promoted";
        }

        user.setRole("ADMIN");
        userRepository.save(user);

        return promoteAdminForm.getUsername() + " has been successfully promoted";
    }

    // ===============================
    // ADD INVENTORY ITEM
    // ===============================
    @PostMapping("/inventory/add")
    @ResponseBody
    public String addInventory(@ModelAttribute InventoryItemForm inventoryForm) {

        if (inventoryForm.getTitle() == null || inventoryForm.getTitle().trim().isEmpty()) {
            return "Name is required.";
        }
        if (inventoryForm.getDescription() == null || inventoryForm.getDescription().trim().isEmpty()) {
            return "Description is required.";
        }
        if (inventoryForm.getPrice() == null) {
            return "Price is required.";
        }

        MultipartFile image = inventoryForm.getImage();
        String imageUrl = null;

        if (image != null && !image.isEmpty()) {
            try {
                String base64 = Base64.getEncoder().encodeToString(image.getBytes());
                imageUrl = "data:" + image.getContentType() + ";base64," + base64;
            } catch (IOException e) {
                return "Failed to save image.";
            }
        }

        ArtPiece artPiece = new ArtPiece();
        artPiece.setTitle(inventoryForm.getTitle());
        artPiece.setDescription(inventoryForm.getDescription());
        artPiece.setPrice(inventoryForm.getPrice());
        artPiece.setImageUrl(imageUrl);

        // IMPORTANT: new art is available + has a createdAt timestamp
        artPiece.setActive(true);
        artPiece.setCreatedAt(LocalDateTime.now());

        artPieceRepository.save(artPiece);
        return "Inventory Item successfully added.";
    }
}
