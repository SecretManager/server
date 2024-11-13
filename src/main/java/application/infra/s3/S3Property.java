package application.infra.s3;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws.s3")
public record S3Property(
        String accessKey,
        String secretKey,
        String region,
        String bucketName
) {
}
