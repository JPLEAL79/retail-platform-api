package com.jp.testplatformapi.controller;

import com.jp.testplatformapi.dto.request.ProductoRequest;
import com.jp.testplatformapi.dto.response.ProductoResponse;
import com.jp.testplatformapi.mapper.ProductoMapper;
import com.jp.testplatformapi.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    // The API returns DTOs so the outside world does not depend on JPA internals.
    @GetMapping
    public List<ProductoResponse> getAll() {
        return service.getAll()
                .stream()
                .map(ProductoMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ProductoResponse getById(@PathVariable Long id) {
        return ProductoMapper.toResponse(service.getById(id));
    }

    @GetMapping("/sku/{sku}")
    public ProductoResponse getBySku(@PathVariable String sku) {
        return ProductoMapper.toResponse(service.getBySku(sku));
    }

    @PostMapping
    public ProductoResponse create(@Valid @RequestBody ProductoRequest request) {
        return ProductoMapper.toResponse(service.create(ProductoMapper.toEntity(request)));
    }
}
