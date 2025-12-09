package artstore.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;

@Getter
@Setter
public class InventoryItemForm {
    private String title;
    private String description;
    private BigDecimal price;
    private MultipartFile image;
}
