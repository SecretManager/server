package application.infra.s3;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockS3ApiClient extends S3ApiClient {

    private final Map<String, byte[]> store = new HashMap<>();

    public MockS3ApiClient() {
        super(null, new S3Property("", "", "", ""));
        log.info("Mock S3 Client use!");
    }

    @Override
    public byte[] downloadByteFile(String fileName) {
        return store.get(fileName);
    }

    @Override
    public void uploadByteUsingStream(byte[] fileContent, String fileName) {
        store.put(fileName, fileContent);
    }

    @Override
    public void deleteFile(String fileName) {
        store.remove(fileName);
    }
}
