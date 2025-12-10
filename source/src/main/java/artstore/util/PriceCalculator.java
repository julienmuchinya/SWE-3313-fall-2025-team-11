package artstore.util;

import artstore.entity.CartItem;
import artstore.entity.Order;

import java.math.BigDecimal;

public class PriceCalculator {

    public static BigDecimal calculateTotal(Order order) {
        if (order == null || order.getItems() == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : order.getItems()) {
            if (item == null) continue;

            BigDecimal unit = item.getUnitPrice();

            // fallback to ArtPiece price
            if (unit == null && item.getArtPiece() != null) {
                unit = item.getArtPiece().getPrice();
            }

            if (unit == null) continue;

            Integer qty = item.getQuantity();
            if (qty == null || qty <= 0) {
                qty = 1;
            }

            total = total.add(unit.multiply(BigDecimal.valueOf(qty)));
        }

        return total;
    }
}
