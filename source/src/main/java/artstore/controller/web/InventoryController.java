package artstore.controller;

import artstore.entity.ArtPiece;
import artstore.repository.ArtPieceRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class InventoryController {

    private final ArtPieceRepository artPieceRepository;

    public InventoryController(ArtPieceRepository artPieceRepository) {
        this.artPieceRepository = artPieceRepository;
    }

    @GetMapping("/inventory")
    public String showInventory(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Model model
    ) {

        List<ArtPiece> pieces;

        boolean hasSearch = q != null && !q.isBlank();
        boolean hasMin = minPrice != null;
        boolean hasMax = maxPrice != null;

        if (!hasSearch && !hasMin && !hasMax) {
            pieces = artPieceRepository.findByIsActiveTrueAndOrderItemIsNull();
        } else if (hasSearch && !hasMin && !hasMax) {
            pieces = artPieceRepository
                    .findByIsActiveTrueAndOrderItemIsNullAndTitleContainingIgnoreCase(q);
        } else if (!hasSearch && hasMin && hasMax) {
            pieces = artPieceRepository
                    .findByIsActiveTrueAndOrderItemIsNullAndPriceBetween(minPrice, maxPrice);
        } else if (hasSearch && hasMin && hasMax) {
            pieces = artPieceRepository
                    .findByIsActiveTrueAndOrderItemIsNullAndTitleContainingIgnoreCaseAndPriceBetween(
                            q, minPrice, maxPrice
                    );
        } else {
            // fallback: just default inventory if combination not handled
            pieces = artPieceRepository.findByIsActiveTrueAndOrderItemIsNull();
        }

        model.addAttribute("artPieces", pieces);
        model.addAttribute("q", q);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        return "inventory";
    }
}
