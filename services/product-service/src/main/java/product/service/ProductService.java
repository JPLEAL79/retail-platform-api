package product.service;

import product.entity.Product;
import common.exception.ConflictException;
import common.exception.ResourceNotFoundException;
import product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Page<Product> getAll(Boolean active, int page, int size) {
        Pageable pageable = buildPageable(page, size);

        if (active != null) {
            return repository.findByActiveOrderByIdAsc(active, pageable);
        }

        return repository.findAllByOrderByIdAsc(pageable);
    }

    public Product getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id + "."));
    }

    public Product getBySku(String sku) {
        return repository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU " + sku + "."));
    }

    @Transactional
    public Product create(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }

        if (repository.existsBySku(product.getSku())) {
            throw new ConflictException("A product with this SKU already exists.");
        }

        return repository.save(product);
    }

    @Transactional
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

    @Transactional
    public void delete(Long id) {
        Product product = getById(id);

        // We keep the row to preserve catalog and order history.
        product.setActive(false);
        repository.save(product);
    }

    @Transactional
    public void adjustStock(Long id, Integer delta) {
        if (delta == null) {
            throw new IllegalArgumentException("Stock delta cannot be null.");
        }

        if (delta == 0) {
            return;
        }

        Product product = getById(id);
        int updatedStock = product.getStock() + delta;

        if (updatedStock < 0) {
            throw new ConflictException("Product " + id + " does not have enough stock.");
        }

        // Stock changes are applied explicitly so inventory stays in sync with orders.
        product.setStock(updatedStock);
        repository.save(product);
    }

    private Pageable buildPageable(int page, int size) {
        int resolvedPage = Math.max(page, 0);
        int resolvedSize = Math.min(Math.max(size, 1), 100);
        return PageRequest.of(resolvedPage, resolvedSize);
    }
}
