package javachip.controller.inquiry;

import javachip.dto.inquiry.InquiryRequest;
import javachip.dto.inquiry.InquiryResponse;
import javachip.service.inquiry.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    /**
     * 문의 등록 (구매자)
     */
    @PostMapping
    public ResponseEntity<InquiryResponse> createInquiry(@RequestBody InquiryRequest request) {
        InquiryResponse response = inquiryService.createInquiry(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 상품별 문의 목록 조회 (공개)
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<InquiryResponse>> getInquiriesByProduct(@PathVariable Long productId) {
        List<InquiryResponse> responseList = inquiryService.getInquiriesByProduct(productId);
        return ResponseEntity.ok(responseList);
    }

    /**
     * 문의에 대한 판매자 답변 등록
     */
    @PatchMapping("/{inquiryId}/answer")
    public ResponseEntity<InquiryResponse> addAnswer(
            @PathVariable Long inquiryId,
            @RequestParam String sellerId,
            @RequestBody String answer
    ) {
        InquiryResponse response = inquiryService.addAnswer(inquiryId, sellerId, answer);
        return ResponseEntity.ok(response);
    }

    /**
     * 문의 삭제 (구매자 본인만 가능)
     */
    @DeleteMapping("/{inquiryId}")
    public ResponseEntity<Void> deleteInquiry(
            @PathVariable Long inquiryId,
            @RequestParam String userId
    ) {
        inquiryService.deleteInquiry(inquiryId, userId);
        return ResponseEntity.noContent().build();
    }
}
