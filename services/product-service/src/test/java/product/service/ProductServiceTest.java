package product.service;

import common.exception.ConflictException;
import common.exception.ResourceNotFoundException;
import product.entity.Product;
import product.repository.ProductRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    private final ProductRepository repository = mock(ProductRepository.class);
    private final ProductService service = new ProductService(repository);

    // SKU is the product business identifier, so duplicates must be blocked early.
    @Test
    void createRejectsDuplicatedSku() {
        Product product = product(10);
        when(repository.existsBySku(product.getSku())).thenReturn(true);

        assertThrows(ConflictException.class, () -> service.create(product));

        verify(repository).existsBySku(product.getSku());
        verify(repository, never()).save(product);
    }

    // Stock adjustments can add or remove units, but available stock cannot become negative.
    @Test
    void adjustStockRejectsStockBelowZero() {
        Product product = product(3);
        when(repository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(ConflictException.class, () -> service.adjustStock(1L, -4));

        assertEquals(3, product.getStock());
        verify(repository, never()).save(product);
    }

    // Missing resources should fail with the same domain exception used by the API layer.
    @Test
    void getByIdRejectsMissingProduct() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    private Product product(int stock) {
        Product product = new Product();
        product.setSku("SKU-001");
        product.setName("Notebook Lenovo");
        product.setBrand("Lenovo");
        product.setPrice(BigDecimal.valueOf(550000));
        product.setStock(stock);
        return product;
    }
}
