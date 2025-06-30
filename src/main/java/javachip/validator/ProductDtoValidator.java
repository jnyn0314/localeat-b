package javachip.validator;

import javachip.dto.product.ProductDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator; // jakarta가 아니라 얘는 springframework로 시작하는 거 import해야 오류안나요

@Component
public class ProductDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductDto.class.equals(clazz);
    }
    @Override
    public void validate(Object target, Errors errors) {
        ProductDto dto = (ProductDto) target;
        if (Boolean.TRUE.equals(dto.getIsGroupBuy()) && (dto.getMaxParticipants() == null || dto.getMaxParticipants() <= 0)) {
            errors.rejectValue("maxParticipants", "product.maxParticipants.requiredIfGroupBuy");
        }
    }
}
