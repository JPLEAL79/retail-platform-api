package product.service;

import product.entity.Product;
import common.exception.ConflictException;
import common.exception.ResourceNotFoundException;
import product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAll() {
        return repository.findAll();
    }

    public Product getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id + "."));
    }

    public Product getBySku(String sku) {
        return repository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU " + sku + "."));
    }

    public Product create(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }

        if (repository.existsBySku(product.getSku())) {
            throw new ConflictException("A product with this SKU already exists.");
        }

        return repository.save(product);
    }

    public Product update(Long id, Product updatedProduct) {
        if (updatedProduct == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }

        Product existingProduct = getById(id);

        if (repository.existsBySkuAndIdNot(updatedProduct.getSku(), id)) {
            throw new ConflictException("A product with this SKU already exists.");
        }

        existingProduct.setSku(updatedProduct.getSku());
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setBrand(updatedProduct.getBrand());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStock(updatedProduct.getStock());
        existingProduct.setActive(updatedProduct.getActive());

        return repository.save(existingProduct);
    }

    public void delete(Long id) {
        Product product = getById(id);

        // We keep the row to preserve catalog and order history.
        product.setActive(false);
        repository.save(product);
    }
}
