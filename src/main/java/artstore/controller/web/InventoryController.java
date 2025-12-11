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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Math.ceil;
import static java.lang.Math.min;

@Controller
public class InventoryController {

    private final ArtPieceRepository artPieceRepository;

    public InventoryController(ArtPieceRepository artPieceRepository) {
        this.artPieceRepository = artPieceRepository;
    }

    @GetMapping("/inventory")
    public String inventory(
            Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(required = false) String artistFilter,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice
    ) {
        List<ArtPiece> artPieces;

        // Apply search and filters
        if (search != null && !search.trim().isEmpty()) {
            if (minPrice != null || maxPrice != null) {
                BigDecimal min = minPrice != null ? minPrice : BigDecimal.ZERO;
                BigDecimal max = maxPrice != null ? maxPrice : new BigDecimal("999999");
                artPieces = artPieceRepository.searchWithPriceRange(search.trim(), min, max);
            } else {
                artPieces = artPieceRepository.searchAvailableArtPieces(search.trim());
            }
        } else if (minPrice != null || maxPrice != null) {
            BigDecimal min = minPrice != null ? minPrice : BigDecimal.ZERO;
            BigDecimal max = maxPrice != null ? maxPrice : new BigDecimal("999999");
            artPieces = artPieceRepository.findByPriceRange(min, max);
        } else {
            artPieces = artPieceRepository.findByIsActiveTrueAndOrderItemIsNull();
        }

        // Apply artist filter
        if (artistFilter != null && !artistFilter.trim().isEmpty()) {
            artPieces = artPieces.stream()
                    .filter(p -> p.getArtistName() != null && 
                            p.getArtistName().toLowerCase().contains(artistFilter.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Apply sorting
        if (sortBy != null && !sortBy.isEmpty()) {
            String order = (sortOrder != null && sortOrder.equalsIgnoreCase("asc")) ? "asc" : "desc";
            
            switch (sortBy.toLowerCase()) {
                case "price":
                    if (order.equals("asc")) {
                        artPieces = artPieces.stream()
                                .sorted(Comparator.comparing(ArtPiece::getPrice))
                                .collect(Collectors.toList());
                    } else {
                        artPieces = artPieces.stream()
                                .sorted(Comparator.comparing(ArtPiece::getPrice).reversed())
                                .collect(Collectors.toList());
                    }
                    break;
                case "title":
                    if (order.equals("asc")) {
                        artPieces = artPieces.stream()
                                .sorted(Comparator.comparing(ArtPiece::getTitle, String.CASE_INSENSITIVE_ORDER))
                                .collect(Collectors.toList());
                    } else {
                        artPieces = artPieces.stream()
                                .sorted(Comparator.comparing(ArtPiece::getTitle, String.CASE_INSENSITIVE_ORDER).reversed())
                                .collect(Collectors.toList());
                    }
                    break;
                case "artist":
                    if (order.equals("asc")) {
                        artPieces = artPieces.stream()
                                .sorted(Comparator.comparing(ArtPiece::getArtistName, String.CASE_INSENSITIVE_ORDER))
                                .collect(Collectors.toList());
                    } else {
                        artPieces = artPieces.stream()
                                .sorted(Comparator.comparing(ArtPiece::getArtistName, String.CASE_INSENSITIVE_ORDER).reversed())
                                .collect(Collectors.toList());
                    }
                    break;
                default:
                    // Default: price descending
                    artPieces = artPieces.stream()
                            .sorted(Comparator.comparing(ArtPiece::getPrice).reversed())
                            .collect(Collectors.toList());
            }
        } else {
            // Default sorting: price descending
            artPieces = artPieces.stream()
                    .sorted(Comparator.comparing(ArtPiece::getPrice).reversed())
                    .collect(Collectors.toList());
        }

        int artPieceSize = artPieces.size();
        int PAGE_SIZE = 9;
        int pageCount = (int) ceil((double) artPieceSize / PAGE_SIZE);

        int start = (page - 1) * PAGE_SIZE;
        int end = min(start + PAGE_SIZE, artPieceSize);

        List<ArtPiece> artPiecesPage = artPieces.subList(start, end);

        // Get unique artists for filter dropdown (from all available items)
        List<ArtPiece> allAvailable = artPieceRepository.findByIsActiveTrueAndOrderItemIsNull();
        Set<String> artists = allAvailable.stream()
                .map(ArtPiece::getArtistName)
                .filter(name -> name != null && !name.isEmpty())
                .sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new));

        // Get price range for filter (from all available items)
        BigDecimal minPriceInDb = allAvailable.stream()
                .map(ArtPiece::getPrice)
                .min(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);
        BigDecimal maxPriceInDb = allAvailable.stream()
                .map(ArtPiece::getPrice)
                .max(Comparator.naturalOrder())
                .orElse(new BigDecimal("1000"));

        model.addAttribute("artPieces", artPiecesPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("search", search != null ? search : "");
        model.addAttribute("sortBy", sortBy != null ? sortBy : "price");
        model.addAttribute("sortOrder", sortOrder != null ? sortOrder : "desc");
        model.addAttribute("artistFilter", artistFilter != null ? artistFilter : "");
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("artists", artists);
        model.addAttribute("minPriceInDb", minPriceInDb);
        model.addAttribute("maxPriceInDb", maxPriceInDb);
        model.addAttribute("totalResults", artPieceSize);

        return "inventory";
    }

    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Integer productId, HttpSession session) {
        // only add if active + not sold
        artPieceRepository.findByProductIdAndIsActiveTrueAndOrderItemIsNull(productId)
                .ifPresent(p -> SessionUtil.addProductToCart(session, productId));

        return "redirect:/shopping-cart";
    }
}
