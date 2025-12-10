package artstore.controller.web;


import artstore.entity.ArtPiece;
import artstore.entity.Order;
import artstore.entity.User;
import artstore.model.InventoryItemForm;
import artstore.model.PromoteAdminForm;


import artstore.repository.ArtPieceRepository;
import artstore.repository.OrderRepository;
import artstore.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

import java.util.Base64;

import java.util.List;
import java.util.Optional;


@Controller
public class AdminPageController{
    private final ArtPieceRepository ArtPieceRepository;
    private final UserRepository UserRepository;
    private final OrderRepository OrderRepository;

    public AdminPageController(ArtPieceRepository artPieceRepository, UserRepository userRepository, OrderRepository orderRepository) {
        ArtPieceRepository = artPieceRepository;
        UserRepository = userRepository;
        OrderRepository = orderRepository;
    }

    @GetMapping("/admin-page")
    public String adminPage(Model model, HttpSession session) {

        List<Order> orders = OrderRepository.findAll();

        model.addAttribute("promoteAdminForm", new PromoteAdminForm());
        model.addAttribute("inventoryItemForm", new InventoryItemForm());
        model.addAttribute("orders",orders);

        try {
            Object role = session.getAttribute("userRole");
            if (role == null || !role.toString().equals("ADMIN")) {
                System.out.println(role.toString());
                return "redirect:/index";
            }
            return "admin-page";
        } catch (Exception exception) {
            exception.printStackTrace();
            return "admin-page";
        }
}

    @PostMapping("/promote/admin")
    @ResponseBody
    public String promoteAdmin(@ModelAttribute PromoteAdminForm promoteAdminForm, Model model) {

        if (promoteAdminForm.getUsername() == null || promoteAdminForm.getUsername().trim().isEmpty()) {
            return "Username is required";
        }
        if(!promoteAdminForm.getUsername().equals(promoteAdminForm.getConfirmUsername())) {
            return "Username and/or Confirm Username is incorrect";
        }
        Optional<User> user = UserRepository.findByUsername(promoteAdminForm.getUsername());
        if(user.isPresent()) {
            if (user.get().getRole().equals("USER")) {
                user.get().setRole("ADMIN");
                UserRepository.save(user.get());
                return promoteAdminForm.getUsername() + " has been successfully promoted";
            } else {
                return promoteAdminForm.getUsername() + " is already promoted";
            }
        } else {
            return promoteAdminForm.getUsername() + " does not match any known Users";
        }
    }







    @PostMapping("/inventory/add")
    @ResponseBody
    public String addInventory(@ModelAttribute InventoryItemForm inventoryForm, Model model) {

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
        try {
            imageUrl = Base64.getEncoder().encodeToString(image.getBytes());
            imageUrl = "data:" + image.getContentType() + ";base64," + imageUrl;
        } catch (IOException e) {
            return "Failed to save image.";
        }



        ArtPiece artPiece = new ArtPiece();
        artPiece.setTitle(inventoryForm.getTitle());
        artPiece.setDescription(inventoryForm.getDescription());
        artPiece.setPrice(inventoryForm.getPrice());
        artPiece.setImageUrl(imageUrl);

        ArtPieceRepository.save(artPiece);
        return "Inventory Item successfully added.";
    }

}

