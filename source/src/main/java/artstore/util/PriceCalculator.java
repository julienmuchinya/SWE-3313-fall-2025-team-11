package artstore.util;

import artstore.entity.CartItem;
import artstore.entity.Order;

import java.math.BigDecimal;

public class PriceCalculator {

    private PriceCalculator() {
    }

    public static BigDecimal calculateTotal(Order order) {
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : order.getItems()) {
            if (item.getUnitPrice() != null && item.getQuantity() != null) {
                BigDecimal line = item.getUnitPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity()));
                total = total.add(line);
            }
        }

        return total;
    }
}
