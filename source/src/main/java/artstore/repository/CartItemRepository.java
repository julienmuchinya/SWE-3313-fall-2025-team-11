package artstore.repository;

import artstore.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    // Projection for the sales report
    // Note: COUNT() returns Long in JPQL, so we keep Long here even though entity IDs are Integer
    interface SalesReportRow {
        String getTitle();
        Long getUnitsSold();
        BigDecimal getTotalRevenue();
    }

    @Query("""
        SELECT
            ci.artPiece.title        AS title,
            COUNT(ci)                AS unitsSold,
            SUM(ci.artPiece.price)   AS totalRevenue
        FROM CartItem ci
        GROUP BY ci.artPiece.title
        """)
    List<SalesReportRow> getSalesReport();
}
