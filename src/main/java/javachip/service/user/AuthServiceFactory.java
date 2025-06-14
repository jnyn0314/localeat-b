package javachip.service.user;

import javachip.entity.user.UserRole;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AuthServiceFactory {
    private final Map<UserRole, AuthService> serviceMap;

    public AuthServiceFactory(
            @Qualifier("AuthServiceConsumer") AuthService consumerService,
            @Qualifier("AuthServiceSeller") AuthService sellerService
    ) {
        this.serviceMap = Map.of(
                UserRole.CONSUMER, consumerService,
                UserRole.SELLER, sellerService
        );
    }

    public AuthService getService(UserRole role) {
        return serviceMap.get(role);
    }
}
