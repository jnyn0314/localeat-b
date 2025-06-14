package javachip.controller.enums;

import javachip.entity.product.LocalType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class EnumController {

    @GetMapping("/local")
    public List<String> getLocalTypes() {
        return Arrays.stream(LocalType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @GetMapping("/group-buy")
    public List<String> getGroupBuyOptions() {
        return List.of("O", "X");
    }

    @GetMapping("/grade-b")
    public List<String> getGradeBOptions() {
        return List.of("O", "X");
    }
}
