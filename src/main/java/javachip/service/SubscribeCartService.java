package javachip.service;

import javachip.dto.subscription.SubscribeCartItemRequest;
import javachip.dto.subscription.SubscribeCartItemResponse;

import java.util.List;

public interface SubscribeCartService{
    List<SubscribeCartItemResponse> getItems(String consumerId); // 수정됨
    void deleteItem(Long cartItemId);
    void toggleItem(Long cartItemId, boolean selected);
    void toggleAll(String consumerId, boolean selected);
    void addItem(String userId, SubscribeCartItemRequest request);
}
