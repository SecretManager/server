package application.infra.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class S3ApiClient {

    private final AmazonS3 s3Client;
    private final String bucketName;

    public S3ApiClient(AmazonS3 s3Client, S3Property property) {
        this.s3Client = s3Client;
        this.bucketName = property.bucketName();
    }

    public void uploadByteUsingStream(byte[] fileContent, String fileName) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(fileContent.length);
            s3Client.putObject(bucketName, fileName, new ByteArrayInputStream(fileContent), metadata);
        } catch (Exception e) {
            log.error("Failed to upload file. fileName: {}, errorMessage: {}", fileName, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public byte[] downloadByteFile(String fileName) {
        try {
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, fileName);
            S3Object s3Object = s3Client.getObject(getObjectRequest);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            try (S3ObjectInputStream inputStream = s3Object.getObjectContent()) {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("Failed to download file. fileName: {}, errorMessage: {}", fileName, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
