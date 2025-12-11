package artstore.controller.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentForm {

    // who is paying
    private Integer userId;

    // which products are in the order
    private List<Integer> productIds;

    // GROUND, THREE_DAY, OVERNIGHT
    private String shippingMethod;

    // extra info (if needed by your views)
    private String fullName;
    private String address;
    private String phoneNumber;
    private String zipCode;

    // raw card number (we will only show last 4 in UI, not store full)
    private String cardNumber;
}
