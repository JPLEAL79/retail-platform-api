package order.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class DeliveryAddressRequest {

    @NotBlank(message = "Region is required.")
    @Size(max = 100, message = "Region must not exceed 100 characters.")
    @Pattern(regexp = "^$|.*\\p{L}.*", message = "Region must contain valid text.")
    @JsonProperty("region")
    private String region;

    @NotBlank(message = "City is required.")
    @Size(max = 100, message = "City must not exceed 100 characters.")
    @Pattern(regexp = "^$|.*\\p{L}.*", message = "City must contain valid text.")
    @JsonProperty("ciudad")
    @JsonAlias("city")
    private String city;

    @NotBlank(message = "Street is required.")
    @Size(max = 150, message = "Street must not exceed 150 characters.")
    @Pattern(regexp = "^$|.*\\p{L}.*", message = "Street must contain valid text.")
    @JsonProperty("calle")
    @JsonAlias("street")
    private String street;

    @NotBlank(message = "Street number is required.")
    @Size(max = 20, message = "Street number must not exceed 20 characters.")
    @Pattern(regexp = "^$|^[0-9]+$", message = "Street number must contain only digits.")
    @JsonProperty("numero")
    @JsonAlias("streetNumber")
    private String streetNumber;

    @Size(max = 50, message = "Apartment number must not exceed 50 characters.")
    @JsonProperty("numeroDepto")
    @JsonAlias("apartmentNumber")
    private String apartmentNumber;

    @Size(max = 200, message = "Reference must not exceed 200 characters.")
    @JsonProperty("referencia")
    @JsonAlias("reference")
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
