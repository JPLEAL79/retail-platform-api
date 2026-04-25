package order.dto.response;

public class DeliveryAddressResponse {

    private String region;
    @com.fasterxml.jackson.annotation.JsonProperty("ciudad")
    private String city;
    @com.fasterxml.jackson.annotation.JsonProperty("calle")
    private String street;
    @com.fasterxml.jackson.annotation.JsonProperty("numero")
    private String streetNumber;
    @com.fasterxml.jackson.annotation.JsonProperty("numeroDepto")
    private String apartmentNumber;
    @com.fasterxml.jackson.annotation.JsonProperty("referencia")
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
