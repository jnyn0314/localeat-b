/*
파일명 : OrderStatusUpdateRequest.java
파일설명 : PUT /api/seller/orders/{orderItemId} 요청 바디
작성자 : 김민하
기간 : 2025-05.23.
*/
package javachip.dto.order.seller;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusUpdateRequest {
    private String status;
}

