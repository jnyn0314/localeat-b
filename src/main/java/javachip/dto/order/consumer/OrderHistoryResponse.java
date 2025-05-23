package javachip.dto.order.consumer;

import javachip.dto.order.OrderItemDto;
import javachip.entity.Orders;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderHistoryResponse {
    private Long orderId;
    private String orderDate;
    private List<OrderItemDto> items;

    public OrderHistoryResponse(Long orderId, java.time.LocalDateTime orderDate, List<OrderItemDto> items) {
        this.orderId = orderId;
        this.orderDate = orderDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.items = items;
    }

    public static OrderHistoryResponse fromEntity(Orders order) {
        return new OrderHistoryResponse(
                order.getId(),
                order.getCreatedAt(),
                order.getOrderItems().stream()
                        .map(OrderItemDto::fromEntity)
                        .collect(Collectors.toList())
        );
    }
}