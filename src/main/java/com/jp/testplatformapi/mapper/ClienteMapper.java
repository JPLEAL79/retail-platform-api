package com.jp.testplatformapi.mapper;

import com.jp.testplatformapi.dto.request.ClienteRequest;
import com.jp.testplatformapi.dto.response.ClienteResponse;
import com.jp.testplatformapi.entity.Cliente;

public final class ClienteMapper {

    private ClienteMapper() {
    }

    public static Cliente toEntity(ClienteRequest request) {
        Cliente cliente = new Cliente();
        cliente.setRut(request.getRut());
        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setCorreo(request.getCorreo());
        cliente.setTelefono(request.getTelefono());
        return cliente;
    }

    public static ClienteResponse toResponse(Cliente cliente) {
        ClienteResponse response = new ClienteResponse();
        response.setId(cliente.getId());
        response.setRut(cliente.getRut());
        response.setNombre(cliente.getNombre());
        response.setApellido(cliente.getApellido());
        response.setCorreo(cliente.getCorreo());
        response.setTelefono(cliente.getTelefono());
        return response;
    }
}
