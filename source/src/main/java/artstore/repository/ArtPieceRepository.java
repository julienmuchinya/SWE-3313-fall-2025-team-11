package artstore.repository;

import artstore.entity.ArtPiece;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ArtPieceRepository extends JpaRepository<ArtPiece, Integer> {

    // inventory: active and not yet in any order
    List<ArtPiece> findByIsActiveTrueAndOrderItemIsNull();

    // add-to-cart + checkout: still active and not sold
    Optional<ArtPiece> findByProductIdAndIsActiveTrueAndOrderItemIsNull(Long productId);

    // search by title
    List<ArtPiece> findByIsActiveTrueAndOrderItemIsNullAndTitleContainingIgnoreCase(String title);

    // price range
    List<ArtPiece> findByIsActiveTrueAndOrderItemIsNullAndPriceBetween(
            BigDecimal min, BigDecimal max
    );

    // title + price range
    List<ArtPiece> findByIsActiveTrueAndOrderItemIsNullAndTitleContainingIgnoreCaseAndPriceBetween(
            String title,
            BigDecimal min,
            BigDecimal max
    );
}
