package artstore.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class PayNowForm {
    private String fullName;
    private String address;
    private String zipCode;
    private String phoneNumber;
    private String shippingMethod;
    private String creditCardNumber;
    private String creditCardExpiryDate;
    private String creditCardCvv;
}
