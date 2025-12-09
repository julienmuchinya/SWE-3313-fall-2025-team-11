package artstore.controller.web;

import artstore.entity.ArtPiece;
import artstore.entity.CartItem;
import artstore.repository.ArtPieceRepository;
import artstore.repository.CartItemRepository;
import artstore.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

import static java.lang.Math.ceil;
import static java.lang.Math.min;

@Controller
public class InventoryController {

    private final ArtPieceRepository artPieceRepository;
    private final CartItemRepository cartItemRepository;

    public InventoryController(ArtPieceRepository artPieceRepository, CartItemRepository cartItemRepository) {
        this.artPieceRepository = artPieceRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @GetMapping("/inventory")
    public String inventory(Model model, @RequestParam(defaultValue = "1") int page) {
        List<ArtPiece> unsortedArtPieces = artPieceRepository.findByIsActiveTrueAndOrderItemIsNull();
        List<ArtPiece> artPieces = unsortedArtPieces.stream().sorted(Comparator.comparing(ArtPiece::getPrice)).toList().reversed();
        int artPieceSize = artPieces.size();
        int PAGE_SIZE = 9;

        int pageCount = (int) ceil((double) artPieceSize / PAGE_SIZE);


        int start = (page - 1) * PAGE_SIZE;
        int end = min(start + PAGE_SIZE, artPieceSize);

        List<ArtPiece> artPiecesPage = artPieces.subList(start, end);

        model.addAttribute("artPieces", unsortedArtPieces);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageCount", pageCount);

        return "inventory";
    }

    @PostMapping("/cart/add/{productId}")
    public ResponseEntity<Map<String, Object>> addToCart(@PathVariable Long productId, HttpSession session) {
        CartItem cartItem = new CartItem();
        Optional<ArtPiece> artPiece = artPieceRepository.findByProductIdAndIsActiveTrueAndOrderItemIsNull(productId);
        cartItem.setArtPiece(artPiece.get());
        cartItemRepository.save(cartItem);
        SessionUtil.addProductToCart(session, cartItem.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);

        return ResponseEntity.ok(response);
    }
}
