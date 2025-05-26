/*
 * 파일명 : SubscribeQueryService.java
 * 파일설명 : 유저별 구독 주문 목록 조회 서비스 인터페이스
 * 작성자 : 정여진
 * 작성일 : 2025-05-26
 */

package javachip.service;

import javachip.dto.subscription.SubscribeOrderResponseDto;

import java.util.List;

public interface SubscribeQueryService {

    /**
     * 주어진 userId에 해당하는 구독 주문 목록을 조회한다.
     * @param userId 사용자 ID
     * @return 구독 주문 응답 DTO 리스트
     */
    List<SubscribeOrderResponseDto> findSubscriptionsByUser(String userId);
}