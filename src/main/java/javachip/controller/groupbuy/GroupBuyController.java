package javachip.controller.groupbuy;

import javachip.dto.groupbuy.GroupBuyParticipationRequest;
import javachip.dto.groupbuy.*;
import javachip.service.groupbuy.GroupBuyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groupBuy")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class GroupBuyController {

    private final GroupBuyService groupBuyService;
    private final GroupBuyService gbService;

    /**
     * 공동구매 생성 (소비자)
     * 프론트가 userId를 헤더나 바디로 함께 보내야 함
     */
    @PostMapping("/create")
    public ResponseEntity<GroupBuyCreateResponse> createGroupBuy(
            @RequestBody GroupBuyCreateRequest request,
            @RequestHeader("X-USER-ID") String userId) {

        GroupBuyCreateResponse response = groupBuyService.createGroupBuy(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/join")
    public ResponseEntity<?> participateInGroupBuy(
            @RequestBody GroupBuyParticipationRequest request,
            @RequestHeader("X-USER-ID") String userId) {
        groupBuyService.participateInGroupBuy(request.getGroupBuyId(), userId, request.getQuantity());
        return ResponseEntity.ok().build();
    }

    //상세조회
    @GetMapping("/{id}")
    public ResponseEntity<GroupBuyDetailResponse> getDetail(@PathVariable("id") Long id) {
        GroupBuyDetailResponse resp = gbService.getDetail(id);
        return ResponseEntity.ok(resp);
    }

    // 공동구매 리스트 조회 (상품 id로)
    @GetMapping("/list")
    public ResponseEntity<List<GroupBuyListResponse>> getGroupBuyListByProductId(
            @RequestParam("productId") Long productId) {
        List<GroupBuyListResponse> list = groupBuyService.getGroupBuyListByProductId(productId);
        return ResponseEntity.ok(list);
    }

    /**  내가 참여한 공동구매 현황 */
    @GetMapping("/my")
    public ResponseEntity<List<MyGroupBuyStatusResponse>> getMyParticipations(
            @RequestHeader("X-USER-ID") String userId
    ) {
        List<MyGroupBuyStatusResponse> list = groupBuyService.getMyParticipations(userId);
        return ResponseEntity.ok(list);
    }
}