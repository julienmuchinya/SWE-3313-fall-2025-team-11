package artstore.controller.api;

import artstore.entity.ArtPiece;
import artstore.entity.CartItem;
import artstore.entity.Order;
import artstore.entity.User;
import artstore.repository.ArtPieceRepository;
import artstore.repository.OrderRepository;
import artstore.repository.UserRepository;
import artstore.util.PriceCalculator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ArtPieceRepository artPieceRepository;

    public OrderController(OrderRepository orderRepository,
                           UserRepository userRepository,
                           ArtPieceRepository artPieceRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.artPieceRepository = artPieceRepository;
    }

    // POST /api/orders
    @PostMapping
    public Order createOrder(@RequestBody CreateOrderRequest request) {
        User user = userRepository.findById(request.userId()).orElse(null);
        if (user == null) {
            return null;
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        List<CartItem> items = new ArrayList<>();
        for (CreateOrderItemRequest itemRequest : request.items()) {
            ArtPiece artPiece = artPieceRepository.findById(itemRequest.artPieceId())
                    .orElse(null);
            if (artPiece == null) {
                continue;
            }

            CartItem item = new CartItem();
            item.setOrder(order);
            item.setArtPiece(artPiece);
            item.setQuantity(itemRequest.quantity());
            item.setUnitPrice(artPiece.getPrice());

            items.add(item);
        }

        order.setItems(items);
        order.setTotalAmount(PriceCalculator.calculateTotal(order));

        return orderRepository.save(order);
    }

    // GET /api/orders/{id}
    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    // Request body types

    public record CreateOrderRequest(Long userId, List<CreateOrderItemRequest> items) {
    }

    public record CreateOrderItemRequest(Long artPieceId, Integer quantity) {
    }
}
