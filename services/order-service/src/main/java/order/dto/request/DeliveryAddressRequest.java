package order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class DeliveryAddressRequest {

    @NotBlank(message = "Region is required.")
    @Size(max = 100, message = "Region must not exceed 100 characters.")
    @Pattern(regexp = "^$|.*\\p{L}.*", message = "Region must contain valid text.")
    private String region;

    @NotBlank(message = "City is required.")
    @Size(max = 100, message = "City must not exceed 100 characters.")
    @Pattern(regexp = "^$|.*\\p{L}.*", message = "City must contain valid text.")
    private String city;

    @NotBlank(message = "Street is required.")
    @Size(max = 150, message = "Street must not exceed 150 characters.")
    @Pattern(regexp = "^$|.*\\p{L}.*", message = "Street must contain valid text.")
    private String street;

    @NotBlank(message = "Street number is required.")
    @Size(max = 20, message = "Street number must not exceed 20 characters.")
    @Pattern(regexp = "^$|^[0-9]+$", message = "Street number must contain only digits.")
    private String streetNumber;

    @Size(max = 50, message = "Apartment number must not exceed 50 characters.")
    private String apartmentNumber;

    @Size(max = 200, message = "Reference must not exceed 200 characters.")
    private String reference;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
