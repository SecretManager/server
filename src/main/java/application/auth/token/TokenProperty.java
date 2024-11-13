package application.auth.token;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("token")
public record TokenProperty(
        String secretKey,
        long accessTokenExpirationMillis,
        long refreshTokenExpirationMillis
) {
}
