package artstore.controller.web;

import artstore.entity.ArtPiece;
import artstore.repository.ArtPieceRepository;
import artstore.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import static java.lang.Math.ceil;
import static java.lang.Math.min;

@Controller
public class InventoryController {

    private final ArtPieceRepository artPieceRepository;

    public InventoryController(ArtPieceRepository artPieceRepository) {
        this.artPieceRepository = artPieceRepository;
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

    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable int productId, HttpSession session) {
        // only add if active + not sold
        artPieceRepository.findByProductIdAndIsActiveTrueAndOrderItemIsNull(productId)
                .ifPresent(p -> SessionUtil.addProductToCart(session, productId));

        return "redirect:/shopping-cart";
    }
}
