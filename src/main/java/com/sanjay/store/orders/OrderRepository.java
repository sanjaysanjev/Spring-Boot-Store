package com.sanjay.store.orders;

import com.sanjay.store.Users.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    @EntityGraph(attributePaths ="orderItems.product")
    @Query("Select o from Order o where o.customer= :customer")
    List<Order>getOrdersByCustomer(@Param("customer")User customer);

    @EntityGraph(attributePaths ="orderItems.product")
    @Query("Select o from Order o where o.id= :orderId")
    Optional<Order> getOrderWithItems(@Param("orderId")Long orderId);
}
