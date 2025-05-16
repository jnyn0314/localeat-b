/*
파일명 : WishServiceImpl.java
파일설명 : WishService 인터페이스의 구현체로, 찜 기능의 실제 로직을 수행합니다.
작성자 : 정여진
작성일 : 2025-05-17.
*/
package javachip.service.impl;

import javachip.dto.ProductDto;
import javachip.entity.Product;
import javachip.entity.User;
import javachip.entity.Wish;
import javachip.repository.ProductRepository;
import javachip.repository.UserRepository;
import javachip.repository.WishRepository;
import javachip.service.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishServiceImpl implements WishService {

    private final WishRepository wishRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // 찜 상태 토글: 이미 찜했다면 삭제, 아니라면 새로 추가
    @Override
    public boolean toggleWish(String userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 없음"));

        Optional<Wish> existing = wishRepository.findByUserAndProduct(user, product);

        if (existing.isPresent()) {
            wishRepository.delete(existing.get());
            return false; // 삭제됨
        } else {
            wishRepository.save(Wish.builder().user(user).product(product).build());
            return true; // 추가됨
        }
    }

    // 사용자 기준으로 찜한 상품 목록을 조회하여 DTO로 변환
    @Override
    public List<ProductDto> getWishedProducts(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        return wishRepository.findAllByUser(user).stream()
                .map(w -> ProductDto.fromEntity(w.getProduct(), true, w.getId())) // fromEntity 확장 필요
                .collect(Collectors.toList());
    }
}