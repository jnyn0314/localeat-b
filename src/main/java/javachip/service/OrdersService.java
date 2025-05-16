package javachip.service;

import javachip.dto.order.OrderCreateRequest;
import javachip.dto.order.OrderCreateResponse;
import javachip.dto.order.OrderHistoryResponse;

import java.util.List;

public interface OrdersService {
    OrderCreateResponse createSingleOrder(OrderCreateRequest request);
    List<OrderHistoryResponse> getUserOrders(String userId);
}
