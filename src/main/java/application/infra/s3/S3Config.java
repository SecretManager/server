package application.infra.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!test")
@RequiredArgsConstructor
@Configuration
public class S3Config {

    private final S3Property s3Property;

    @Bean
    public AmazonS3 s3Client() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(s3Property.accessKey(), s3Property.secretKey());
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.fromName(s3Property.region()))
                .build();
    }

    @Profile("local")
    @Bean
    public S3ApiClient mockS3ApiClient() {
        return new MockS3ApiClient();
    }

    @Profile({"prod", "stage"})
    @Bean
    public S3ApiClient s3ApiClient() {
        return new S3ApiClient(s3Client(), s3Property);
    }
}
