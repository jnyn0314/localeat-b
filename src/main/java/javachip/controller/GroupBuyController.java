package javachip.controller;

import jakarta.validation.Valid;
import javachip.dto.GroupBuyCreateRequest;
import javachip.dto.GroupBuyCreateResponse;
import javachip.service.GroupBuyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groupbuys")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // 필요 시 프론트 연동
public class GroupBuyController {

    private final GroupBuyService groupBuyService;

    /**
     * 공동구매 생성 (소비자)
     * 프론트가 userId를 헤더나 바디로 함께 보내야 함
     */
    @PostMapping
    public ResponseEntity<GroupBuyCreateResponse> createGroupBuy(
            @RequestBody GroupBuyCreateRequest request,
            @RequestHeader("X-USER-ID") String userId) {

        GroupBuyCreateResponse response = groupBuyService.createGroupBuy(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
