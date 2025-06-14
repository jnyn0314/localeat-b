package javachip.service.order;

import javachip.dto.order.consumer.OrderCreateRequest;
import javachip.dto.order.consumer.OrderCreateResponse;
import javachip.dto.order.consumer.OrderHistoryResponse;

import java.util.List;

public interface OrdersService {
    OrderCreateResponse createSingleOrder(OrderCreateRequest request);
    List<OrderHistoryResponse> getUserOrders(String userId);
}
