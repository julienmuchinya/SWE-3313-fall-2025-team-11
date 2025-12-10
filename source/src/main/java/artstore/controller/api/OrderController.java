package artstore.controller.api;

import artstore.entity.ArtPiece;
import artstore.entity.CartItem;
import artstore.entity.Order;
import artstore.entity.User;
import artstore.repository.ArtPieceRepository;
import artstore.repository.OrderRepository;
import artstore.repository.UserRepository;
import artstore.util.PriceCalculator;
import org.springframework.web.bind.annotation.*;

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
        // find user
        User user = userRepository.findById(request.userId()).orElse(null);
        if (user == null) {
            return null;
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");

        List<CartItem> items = new ArrayList<>();

        for (CreateOrderItemRequest itemRequest : request.items()) {
            // ArtPiece ID is Long in request, Integer in entity -> convert
            int artPieceId = itemRequest.artPieceId().intValue();

            ArtPiece artPiece = artPieceRepository.findById(artPieceId)
                    .orElse(null);

            if (artPiece == null) {
                continue;
            }

            // if you want to block already sold / inactive art:
            if (!artPiece.isActive()) {
                continue;
            }

            CartItem item = new CartItem();
            item.setOrder(order);
            item.setArtPiece(artPiece);

            // unique art: force quantity = 1
            item.setQuantity(1);
            item.setUnitPrice(artPiece.getPrice());

            items.add(item);

            // mark piece as no longer available
            artPiece.setActive(false);
        }

        order.setItems(items);

        // use your existing PriceCalculator util
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

    // quantity is accepted but we ignore it (art is unique, always 1)
    public record CreateOrderItemRequest(Long artPieceId, Integer quantity) {
    }
}
