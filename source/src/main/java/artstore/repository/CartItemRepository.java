package artstore.repository;

import artstore.dto.SalesReportRow;
import artstore.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("""
        SELECT
            ci.artPiece.title AS title,
            COUNT(ci)          AS unitsSold,
            SUM(ci.unitPrice)  AS totalRevenue
        FROM CartItem ci
        GROUP BY ci.artPiece.title
        """)
    List<SalesReportRow> getSalesReport();
}
