package order.mapper;

import order.dto.request.DeliveryAddressRequest;
import order.dto.response.DeliveryAddressResponse;
import order.dto.response.OrderItemResponse;
import order.dto.response.OrderResponse;
import order.entity.DeliveryAddress;
import order.entity.Order;
import order.entity.OrderItem;

import java.util.List;

public final class OrderMapper {

    private OrderMapper() {
    }

    public static OrderResponse toResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setCustomerId(order.getCustomerId());
        response.setTotal(order.getTotal());
        response.setStatus(order.getStatus());
        response.setItems(toItemResponses(order.getItems()));
        response.setDeliveryAddress(toAddressResponse(order.getDeliveryAddress()));
        return response;
    }

    public static DeliveryAddress toAddressEntity(DeliveryAddressRequest request) {
        DeliveryAddress address = new DeliveryAddress();
        address.setRegion(request.getRegion());
        address.setCity(request.getCity());
        address.setStreet(request.getStreet());
        address.setStreetNumber(request.getStreetNumber());
        address.setApartmentNumber(request.getApartmentNumber());
        address.setReference(request.getReference());
        return address;
    }

    private static List<OrderItemResponse> toItemResponses(List<OrderItem> items) {
        return items.stream()
                .map(OrderMapper::toItemResponse)
                .toList();
    }

    private static OrderItemResponse toItemResponse(OrderItem item) {
        OrderItemResponse response = new OrderItemResponse();
        response.setProductId(item.getProductId());
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setSubtotal(item.getSubtotal());
        return response;
    }

    private static DeliveryAddressResponse toAddressResponse(DeliveryAddress address) {
        DeliveryAddressResponse response = new DeliveryAddressResponse();
        response.setRegion(address.getRegion());
        response.setCity(address.getCity());
        response.setStreet(address.getStreet());
        response.setStreetNumber(address.getStreetNumber());
        response.setApartmentNumber(address.getApartmentNumber());
        response.setReference(address.getReference());
        return response;
    }
}
