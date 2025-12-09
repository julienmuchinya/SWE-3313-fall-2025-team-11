package artstore.util;

import artstore.entity.CartItem;
import artstore.entity.Order;

import java.math.BigDecimal;

public final class PriceCalculator {

    private PriceCalculator() {}

    public static BigDecimal calculateTotal(Order order) {
        if (order == null || order.getItems() == null) {
            return BigDecimal.ZERO;
        }

        return order.getItems().stream()
                .map(item -> item.getArtPiece().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
