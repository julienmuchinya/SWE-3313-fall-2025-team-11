package artstore.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemForm {

    private String title;
    private String description;
    private BigDecimal price;
    private MultipartFile image;

    // optional: store artist name if needed
    private String artistName;
}
