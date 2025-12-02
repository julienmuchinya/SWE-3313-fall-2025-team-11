package artstore.repository;

import artstore.entity.ArtPiece;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtPieceRepository extends JpaRepository<ArtPiece, Long> {
}
