package artstore.controller.api;

import artstore.entity.ArtPiece;
import artstore.entity.CartItem;
import artstore.entity.Order;
import artstore.entity.User;
import artstore.repository.ArtPieceRepository;
import artstore.repository.OrderRepository;
import artstore.repository.UserRepository;
import artstore.util.PriceCalculator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ArtPieceRepository artPieceRepository;

    public OrderController(
            OrderRepository orderRepository,
            UserRepository userRepository,
            ArtPieceRepository artPieceRepository
    ) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.artPieceRepository = artPieceRepository;
    }

    /**
     * Create a new order for a user.
     * Body: { "userId": 1, "artPieceIds": [1, 2, 3] }
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request) {

        if (request == null || request.userId() == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<User> userOpt = userRepository.findById(request.userId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        User user = userOpt.get();

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("NEW");

        if (request.artPieceIds() != null) {
            for (Integer artId : request.artPieceIds()) {
                if (artId == null) continue;

                Optional<ArtPiece> artOpt = artPieceRepository.findById(artId);
                if (artOpt.isEmpty()) continue;

                ArtPiece piece = artOpt.get();

                if (!piece.isActive()) {
                    continue;
                }

                CartItem item = new CartItem();
                item.setArtPiece(piece);

                order.addItem(item);

                piece.setActive(false);
                artPieceRepository.save(piece);
            }
        }

        if (order.getItems() == null || order.getItems().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        order.setTotalAmount(PriceCalculator.calculateTotal(order));

        Order saved = orderRepository.save(order);

        return ResponseEntity.ok(saved);
    }

    /**
     * Get a single order by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Integer id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public record CreateOrderRequest(Integer userId, List<Integer> artPieceIds) {}
}
