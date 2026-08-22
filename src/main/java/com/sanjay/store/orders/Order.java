package com.sanjay.store.orders;

import com.sanjay.store.Users.User;
import com.sanjay.store.carts.Cart;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
//import lombok.var;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="orders")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private User customer;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "created_at", insertable = false,updatable = false)
    private LocalDateTime createdAt;

    @Column(name="total_price")
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private Set<OrderItem> orderItems=new LinkedHashSet<>();

    public static Order fromCart(Cart cart, User customer)
    {
        var order=new Order();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalPrice(cart.getTotalPrice());

        cart.getCartItems().forEach(item-> {
                    var OrderItem = new OrderItem(order,item.getProduct(),item.getQuantity());
                    order.orderItems.add(OrderItem);
                }
        );

        return order;
    }

    public boolean isPlacedBy(User customer)
    {
        return this.customer.equals(customer);
    }

}

