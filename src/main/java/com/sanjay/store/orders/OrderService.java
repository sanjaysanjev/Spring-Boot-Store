package com.sanjay.store.orders;

import com.sanjay.store.auth.AuthService;
import lombok.AllArgsConstructor;

//import lombok.var;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {
    private AuthService authService;
    private OrderRepository orderRepository;
    private OrderMapper orderMapper;
    public List<OrderDto> getAllOrders()
    {
        var user=authService.getCurrentUser();
        var order=orderRepository.getOrdersByCustomer(user);
        return order.stream().map(orderMapper::toDto).collect(Collectors.toList());
    }

    public OrderDto getOrder(Long orderId)
    {
        var order=orderRepository.getOrderWithItems(orderId).orElseThrow(OrderNotFoundException::new);

        var user=authService.getCurrentUser();

        if(!order.isPlacedBy(user))
        {
            throw new AccessDeniedException("you don't have access to this order");
        }
        return orderMapper.toDto(order);
    }

}
