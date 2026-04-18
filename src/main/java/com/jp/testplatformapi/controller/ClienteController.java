package com.jp.testplatformapi.controller;

import com.jp.testplatformapi.dto.request.ClienteRequest;
import com.jp.testplatformapi.dto.response.ClienteResponse;
import com.jp.testplatformapi.mapper.ClienteMapper;
import com.jp.testplatformapi.service.ClienteService;
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
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    // Controllers should stay thin: receive HTTP, delegate work, and return DTOs.
    @GetMapping
    public List<ClienteResponse> getAll() {
        return service.getAll()
                .stream()
                .map(ClienteMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ClienteResponse getById(@PathVariable Long id) {
        return ClienteMapper.toResponse(service.getById(id));
    }

    @GetMapping("/rut/{rut}")
    public ClienteResponse getByRut(@PathVariable String rut) {
        return ClienteMapper.toResponse(service.getByRut(rut));
    }

    @PostMapping
    public ClienteResponse create(@Valid @RequestBody ClienteRequest request) {
        return ClienteMapper.toResponse(service.create(ClienteMapper.toEntity(request)));
    }

    @PutMapping("/{id}")
    public ClienteResponse update(@PathVariable Long id, @Valid @RequestBody ClienteRequest request) {
        return ClienteMapper.toResponse(service.update(id, ClienteMapper.toEntity(request)));
    }
}
