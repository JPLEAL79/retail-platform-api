package com.jp.testplatformapi.controller;

import com.jp.testplatformapi.dto.request.OrdenRequest;
import com.jp.testplatformapi.dto.response.OrdenResponse;
import com.jp.testplatformapi.mapper.OrdenMapper;
import com.jp.testplatformapi.service.OrdenService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ordenes")
public class OrdenController {

    private final OrdenService service;

    public OrdenController(OrdenService service) {
        this.service = service;
    }

    // The controller only deals with HTTP and DTO mapping.
    @PostMapping
    public OrdenResponse create(@Valid @RequestBody OrdenRequest request) {
        return OrdenMapper.toResponse(service.create(OrdenMapper.toEntity(request)));
    }

    @GetMapping
    public List<OrdenResponse> getAll() {
        return service.getAll()
                .stream()
                .map(OrdenMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public OrdenResponse getById(@PathVariable Long id) {
        return OrdenMapper.toResponse(service.getById(id));
    }

    @PutMapping("/{id}")
    public OrdenResponse update(@PathVariable Long id, @Valid @RequestBody OrdenRequest request) {
        return OrdenMapper.toResponse(service.update(id, OrdenMapper.toEntity(request)));
    }
}
