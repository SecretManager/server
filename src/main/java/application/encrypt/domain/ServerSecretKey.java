package application.encrypt.domain;

import java.util.Objects;
import javax.crypto.SecretKey;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ServerSecretKey {

    private final SecretKey secretKey;

    @Autowired
    public ServerSecretKey(
            ServerSecretKeyProperty property,
            SecretKeyGenerator secretKeyGenerator
    ) {
        Objects.requireNonNull(property.secretKey, "server secret key cannot be null");
        this.secretKey = secretKeyGenerator.generateAESKey(property.secretKey);
    }

    @ConfigurationProperties("encrypt")
    public record ServerSecretKeyProperty(
            String secretKey
    ) {
    }
}
