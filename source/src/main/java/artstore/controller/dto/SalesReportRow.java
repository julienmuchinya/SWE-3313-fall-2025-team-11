package artstore.dto;

import java.math.BigDecimal;

public interface SalesReportRow {

    String getTitle();

    Long getUnitsSold();

    BigDecimal getTotalRevenue();
}
