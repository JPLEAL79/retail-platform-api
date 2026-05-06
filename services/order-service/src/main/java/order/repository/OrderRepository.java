package order.repository;

import order.entity.Order;
import order.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByOrderByIdAsc(Pageable pageable);

    Page<Order> findByCustomerIdOrderByIdAsc(Long customerId, Pageable pageable);

    Page<Order> findByStatusOrderByIdAsc(OrderStatus status, Pageable pageable);

    Page<Order> findByCustomerIdAndStatusOrderByIdAsc(Long customerId, OrderStatus status, Pageable pageable);
}
