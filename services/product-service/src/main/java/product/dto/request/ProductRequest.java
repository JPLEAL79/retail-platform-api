package product.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ProductRequest {

    @NotBlank(message = "SKU is required.")
    @Size(max = 30, message = "SKU must not exceed 30 characters.")
    @Pattern(regexp = "^$|^[A-Za-z0-9-]+$", message = "SKU can only contain letters, numbers, and hyphens.")
    @JsonProperty("sku")
    private String sku;

    @NotBlank(message = "Name is required.")
    @Size(max = 150, message = "Name must not exceed 150 characters.")
    @Pattern(regexp = "^$|.*\\p{L}.*", message = "Name must contain valid text.")
    @JsonProperty("nombre")
    @JsonAlias("name")
    private String name;

    @NotBlank(message = "Brand is required.")
    @Size(max = 100, message = "Brand must not exceed 100 characters.")
    @Pattern(regexp = "^$|.*\\p{L}.*", message = "Brand must contain valid text.")
    @JsonProperty("marca")
    @JsonAlias("brand")
    private String brand;

    @NotNull(message = "Price is required.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero.")
    @Digits(integer = 12, fraction = 0, message = "CLP price must not have decimals.")
    @JsonProperty("precio")
    @JsonAlias("price")
    private BigDecimal price;

    @NotNull(message = "Stock is required.")
    @PositiveOrZero(message = "Stock cannot be negative.")
    @JsonProperty("stock")
    private Integer stock;

    @NotNull(message = "Active flag is required.")
    @JsonProperty("activo")
    @JsonAlias("active")
    private Boolean active;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
