package product.controller;

import product.dto.request.ProductRequest;
import product.dto.response.ProductResponse;
import product.mapper.ProductMapper;
import product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import product.dto.request.StockAdjustmentRequest;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public java.util.List<ProductResponse> getAll(
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return service.getAll(active, page, size)
                .getContent()
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Long id) {
        return ProductMapper.toResponse(service.getById(id));
    }

    @GetMapping("/sku/{sku}")
    public ProductResponse getBySku(@PathVariable String sku) {
        return ProductMapper.toResponse(service.getBySku(sku));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody ProductRequest request) {
        return ProductMapper.toResponse(service.create(ProductMapper.toEntity(request)));
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return ProductMapper.toResponse(service.update(id, ProductMapper.toEntity(request)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping("/{id}/stock-adjustments")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void adjustStock(@PathVariable Long id, @Valid @RequestBody StockAdjustmentRequest request) {
        service.adjustStock(id, request.getDelta());
    }
}
