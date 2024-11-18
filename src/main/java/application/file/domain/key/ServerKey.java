package application.file.domain.key;

import java.util.Objects;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ServerKey implements Key {

    private final String secretKey;

    @Autowired
    public ServerKey(
            ServerSecretKeyProperty property
    ) {
        Objects.requireNonNull(property.secretKey, "server secret key cannot be null");
        this.secretKey = property.secretKey;
    }

    @Override
    public String getKey() {
        return secretKey;
    }

    @ConfigurationProperties("encrypt")
    public record ServerSecretKeyProperty(
            String secretKey
    ) {
    }
}
