package product.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class ProductResponse {

    private Long id;
    @JsonProperty("sku")
    private String sku;
    @JsonProperty("nombre")
    private String name;
    @JsonProperty("marca")
    private String brand;
    @JsonProperty("precio")
    private BigDecimal price;
    @JsonProperty("stock")
    private Integer stock;
    @JsonProperty("activo")
    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
