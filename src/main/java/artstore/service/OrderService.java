package artstore.service;

import artstore.entity.Order;
import artstore.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // creates an empty order
    public Order createOrder(Integer userId, List<Integer> productIds) {
        Order order = new Order();
        // Set other required fields as needed
        return orderRepository.save(order);
    }

    public Order finalizeOrder(Integer orderId) {
        return orderRepository.getReferenceById(orderId);

    }

    
}