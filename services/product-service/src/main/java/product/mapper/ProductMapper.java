package product.mapper;

import product.dto.request.ProductRequest;
import product.dto.response.ProductResponse;
import product.entity.Product;

public final class ProductMapper {

    private ProductMapper() {
    }

    public static Product toEntity(ProductRequest request) {
        Product product = new Product();
        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setBrand(request.getBrand());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        return product;
    }

    public static ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setSku(product.getSku());
        response.setName(product.getName());
        response.setBrand(product.getBrand());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        return response;
    }
}
