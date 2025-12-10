package artstore.controller.api;

import artstore.entity.ArtPiece;
import artstore.repository.ArtPieceRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/artpieces")
public class ArtPieceController {

    private final ArtPieceRepository artPieceRepository;

    public ArtPieceController(ArtPieceRepository artPieceRepository) {
        this.artPieceRepository = artPieceRepository;
    }

    // GET /api/artpieces
    @GetMapping
    public List<ArtPiece> getAllArtPieces() {
        return artPieceRepository.findAll();
    }

    // GET /api/artpieces/{id}
    @GetMapping("/{id}")
    public ArtPiece getArtPieceById(@PathVariable Integer id) {
        return artPieceRepository.findById(id).orElse(null);
    }
}
