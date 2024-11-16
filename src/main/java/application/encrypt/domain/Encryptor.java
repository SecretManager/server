package application.encrypt.domain;

import application.common.exception.ForbiddenException;
import application.encrypt.domain.key.KeyChain;
import application.encrypt.domain.key.SecretKeyGenerator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Encryptor {

    private static final int NO_STREAM_DATE = -1;
    private static final int ENCODE_BLOCK_SIZE = 1024;
    private static final int AES_GCM_TAG_LENGTH = 128;
    private static final String AES_GCM_TRANSFORMATION = "AES/GCM/NoPadding";

    private final SecretKeyGenerator generator;

    public byte[] encrypt(byte[] input, KeyChain keyChain) {
        byte[] result = input;
        List<SecretKey> encryptKeys = keyChain.getEncryptKeys(generator);
        for (SecretKey key : encryptKeys) {
            result = encryptUsingSingleKey(result, key);
        }
        return result;
    }

    private byte[] encryptUsingSingleKey(byte[] bytes, SecretKey key) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        Nonce nonce = Nonce.generate();
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, key, nonce);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            outputStream.write(nonce.getBytes());
            applyCipher(inputStream, cipher, outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] decrypt(byte[] input, KeyChain keyChain) {
        byte[] result = input;
        List<SecretKey> decryptKeys = keyChain.getDecryptKeys(generator);
        for (SecretKey key : decryptKeys) {
            result = decryptUsingSingleKey(result, key);
        }
        return result;
    }

    private byte[] decryptUsingSingleKey(byte[] bytes, SecretKey key) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        // 암호화된 데이터에서 nonce(12바이트)를 분리
        Nonce nonce = Nonce.extractFromPrefix(inputStream);
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE, key, nonce);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            applyCipher(inputStream, cipher, outputStream);
            return outputStream.toByteArray();
        } catch (AEADBadTagException e) {
            throw new ForbiddenException("암호화 키가 올바르지 않습니다.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Cipher getCipher(int encryptMode, SecretKey key, Nonce nonce) {
        try {
            GCMParameterSpec gcmSpec = new GCMParameterSpec(AES_GCM_TAG_LENGTH, nonce.getBytes());
            Cipher cipher = Cipher.getInstance(AES_GCM_TRANSFORMATION);
            cipher.init(encryptMode, key, gcmSpec);
            return cipher;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void applyCipher(
            InputStream inputStream,
            Cipher cipher,
            ByteArrayOutputStream outputStream
    ) throws Exception {
        byte[] buffer = new byte[ENCODE_BLOCK_SIZE];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != NO_STREAM_DATE) {
            byte[] outputBytes = cipher.update(buffer, 0, bytesRead);
            writeToOutputStream(outputBytes, outputStream);
        }
        byte[] finalBytes = cipher.doFinal();
        writeToOutputStream(finalBytes, outputStream);
    }

    private void writeToOutputStream(byte[] outputBytes, ByteArrayOutputStream outputStream) throws Exception {
        if (outputBytes != null) {
            outputStream.write(outputBytes);
        }
    }
}
