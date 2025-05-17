package javachip.controller;

import javachip.entity.OrderItem;
import javachip.entity.OrderStatus;
import javachip.repository.OrderItemRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminOrderItemController {

    private final OrderItemRepository orderItemRepository;

    public AdminOrderItemController(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @PatchMapping("/orderitem/{id}/status")
    public ResponseEntity<Void> updateOrderItemStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {

        OrderItem item = orderItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문 항목이 존재하지 않습니다."));

        item.setStatus(status);
        orderItemRepository.save(item);

        return ResponseEntity.ok().build();
    }
}
