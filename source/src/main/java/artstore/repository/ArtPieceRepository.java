package artstore.repository;

import artstore.entity.ArtPiece;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ArtPieceRepository extends JpaRepository<ArtPiece, Integer> {

    // for listing all available art pieces (active and not in an order)
    List<ArtPiece> findByIsActiveTrueAndOrderItemIsNull();

    // 🔧 for InventoryController: single piece by id, must be active and not in an order
    Optional<ArtPiece> findByProductIdAndIsActiveTrueAndOrderItemIsNull(Integer productId);

    // Search functionality
    @Query("SELECT a FROM ArtPiece a WHERE a.isActive = true AND a.orderItem IS NULL " +
           "AND (LOWER(a.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(a.artistName) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(a.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<ArtPiece> searchAvailableArtPieces(@Param("query") String query);

    // Filter by price range
    @Query("SELECT a FROM ArtPiece a WHERE a.isActive = true AND a.orderItem IS NULL " +
           "AND a.price >= :minPrice AND a.price <= :maxPrice")
    List<ArtPiece> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);

    // Search with price range
    @Query("SELECT a FROM ArtPiece a WHERE a.isActive = true AND a.orderItem IS NULL " +
           "AND (LOWER(a.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(a.artistName) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(a.description) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "AND a.price >= :minPrice AND a.price <= :maxPrice")
    List<ArtPiece> searchWithPriceRange(@Param("query") String query, 
                                        @Param("minPrice") BigDecimal minPrice, 
                                        @Param("maxPrice") BigDecimal maxPrice);

    // Filter by artist
    @Query("SELECT a FROM ArtPiece a WHERE a.isActive = true AND a.orderItem IS NULL " +
           "AND LOWER(a.artistName) LIKE LOWER(CONCAT('%', :artist, '%'))")
    List<ArtPiece> findByArtist(@Param("artist") String artist);
}
