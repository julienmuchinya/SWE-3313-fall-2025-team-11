package artstore.controller.web;

import artstore.entity.ArtPiece;
import artstore.entity.Order;
import artstore.entity.User;
import artstore.model.InventoryItemForm;
import artstore.model.PromoteAdminForm;

import artstore.repository.ArtPieceRepository;
import artstore.repository.OrderRepository;
import artstore.repository.UserRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
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
    public String adminPage(Model model, jakarta.servlet.http.HttpSession session) {
        // Check if user is admin
        String userRole = (String) session.getAttribute("userRole");
        if (userRole == null || !"ADMIN".equals(userRole)) {
            return "redirect:/sign-in?error=Admin access required";
        }

        List<Order> allOrders = OrderRepository.findAll();
        List<Order> paidOrders = allOrders.stream()
                .filter(order -> "PAID".equals(order.getStatus()))
                .toList();

        BigDecimal grandTotal = paidOrders.stream()
                .map(order -> order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Get all items (both sold and unsold)
        List<ArtPiece> allItems = ArtPieceRepository.findAll();
        List<ArtPiece> unsoldItems = ArtPieceRepository.findByIsActiveTrueAndOrderItemIsNull();
        List<ArtPiece> soldItems = allItems.stream()
                .filter(item -> !item.isActive() || item.getOrderItem() != null)
                .toList();

        model.addAttribute("promoteAdminForm", new PromoteAdminForm());
        model.addAttribute("inventoryItemForm", new InventoryItemForm());
        model.addAttribute("orders", paidOrders);
        model.addAttribute("grandTotal", grandTotal);
        model.addAttribute("totalSales", paidOrders.size());
        model.addAttribute("allItems", allItems);
        model.addAttribute("unsoldItems", unsoldItems);
        model.addAttribute("soldItems", soldItems);

        return "admin-page";
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
        if (inventoryForm.getArtistName() == null || inventoryForm.getArtistName().trim().isEmpty()) {
            return "Artist is required.";
        }
        if (inventoryForm.getDescription() == null || inventoryForm.getDescription().trim().isEmpty()) {
            return "Description is required.";
        }

        if (inventoryForm.getPrice() == null) {
            return "Price is required.";
        }
        
        if (inventoryForm.getImage() == null || inventoryForm.getImage().isEmpty()) {
            return "Image is required.";
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
        artPiece.setArtistName(inventoryForm.getArtistName() != null ? inventoryForm.getArtistName() : "Unknown");
        artPiece.setDescription(inventoryForm.getDescription());
        artPiece.setPrice(inventoryForm.getPrice());
        artPiece.setImageUrl(imageUrl);
        artPiece.setActive(true);

        ArtPieceRepository.save(artPiece);
        return "Inventory Item successfully added.";
    }

    @GetMapping(value = "/inventory/edit/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public java.util.Map<String, Object> getItemForEdit(@PathVariable Integer id) {
        Optional<ArtPiece> artPieceOpt = ArtPieceRepository.findById(id);
        if (artPieceOpt.isPresent()) {
            ArtPiece artPiece = artPieceOpt.get();
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("productId", artPiece.getProductId());
            response.put("title", artPiece.getTitle());
            response.put("artistName", artPiece.getArtistName());
            response.put("description", artPiece.getDescription());
            response.put("price", artPiece.getPrice());
            response.put("existingImageUrl", artPiece.getImageUrl());
            return response;
        }
        return null;
    }

    

    @PostMapping("/inventory/delete/{id}")
    @ResponseBody
    public String deleteInventory(@PathVariable Integer id) {
        try {
            Optional<ArtPiece> artPieceOpt = ArtPieceRepository.findById(id);
            if (artPieceOpt.isEmpty()) {
                return "Item not found.";
            }

            ArtPiece artPiece = artPieceOpt.get();

            // Check if item is sold
            if (!artPiece.isActive() || artPiece.getOrderItem() != null) {
                return "Cannot delete sold items. You can deactivate them instead.";
            }

            // Delete from database
            ArtPieceRepository.delete(artPiece);
            return "Item successfully deleted.";
        } catch (Exception e) {
            return "Error deleting item: " + e.getMessage();
        }
    }
}
