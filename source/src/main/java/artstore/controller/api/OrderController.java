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

    public OrderController(OrderRepository orderRepository,
                           UserRepository userRepository,
                           ArtPieceRepository artPieceRepository) {
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

        // 1) Find the user
        if (request == null || request.userId() == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<User> userOpt = userRepository.findById(request.userId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        User user = userOpt.get();

        // 2) Create a new Order (without total yet)
        Order order = Order.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .status("NEW")
                .paymentMethod("CARD")      // simple default
                .paymentStatus("PENDING")
                .build();

        // 3) Add each art piece as a CartItem (1-of-1)
        if (request.artPieceIds() != null) {
            for (Integer artId : request.artPieceIds()) {
                if (artId == null) continue;

                Optional<ArtPiece> artOpt = artPieceRepository.findById(artId);
                if (artOpt.isEmpty()) continue;

                ArtPiece piece = artOpt.get();

                // skip if already sold/inactive
                if (!piece.isActive()) {
                    continue;
                }

                // Create a CartItem (no quantity, uses art price later)
                CartItem item = new CartItem();
                item.setArtPiece(piece);

                // Attach to order (sets item.order = this)
                order.addItem(item);

                // Mark the art piece as sold / inactive
                piece.setActive(false);
            }
        }

        // 4) If no items were added, don't create the order
        if (order.getItems() == null || order.getItems().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // 5) Calculate total from the art piece prices
        order.setTotalAmount(PriceCalculator.calculateTotal(order));

        // 6) Save order (cascades items)
        Order saved = orderRepository.save(order);

        return ResponseEntity.ok(saved);
    }

    /**
     * Get a single order by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Request body for creating an order.
     * Example JSON:
     * {
     *   "userId": 1,
     *   "artPieceIds": [1, 2, 3]
     * }
     */
    public record CreateOrderRequest(Long userId, List<Integer> artPieceIds) {}
}
