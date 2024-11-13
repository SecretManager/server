//package application;
//
//import application.encryptor.Encryptor;
//import application.encryptor.Nonce;
//import application.encryptor.Sha256SecretKeyGenerator;
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//
//public class FileEncryptor {
//
//  private static Sha256SecretKeyGenerator sha256SecretKeyGenerator = new Sha256SecretKeyGenerator();
//
//  // AES 암호화 키 생성
//  public static SecretKey generateKey() throws Exception {
//    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//    keyGenerator.init(128); // 128비트 키
//    return keyGenerator.generateKey();
//  }
//
//  // 파일을 암호화하고, 암호화된 파일을 복호화
//  public static void encryptFile(File inputFile, File encryptedFile, SecretKey key) throws Exception {
//    Encryptor encryptor = new Encryptor(sha256SecretKeyGenerator);
//    byte[] encrypted = encryptor.encrypt(new FileInputStream(inputFile), key, Nonce.generate());
//    // 암호화된 데이터를 파일로 저장
//    try (FileOutputStream outputStream = new FileOutputStream(encryptedFile)) {
//      outputStream.write(encrypted);
//    }
//  }
//
//  public static void decryptFile(File encryptedFile, File decryptedFile, SecretKey key) throws Exception {
//    Encryptor encryptor = new Encryptor(sha256SecretKeyGenerator);
//    byte[] decrypt = encryptor.decrypt(new FileInputStream(encryptedFile), key);
//
//    // 복호화된 데이터를 파일로 저장
//    try (FileOutputStream outputStream = new FileOutputStream(decryptedFile)) {
//      outputStream.write(decrypt);
//    }
//  }
//
//  public static void main(String[] args) {
//    try {
//      // 암호화할 파일 경로
//      File inputFile = new File("/Users/mallang/Downloads/aa.mov"); // 실제 파일 경로로 수정
//      // 암호화된 파일 경로
//      File encryptedFile = new File(inputFile.getParent(), inputFile.getName() + ".enc");
//
//      // 복호화된 파일 경로
//      File decryptedFile = new File(inputFile.getParent(), "decrypted_" + inputFile.getName());
//
//      // AES 키 생성
//      SecretKey key = generateKey();
//
//      // 파일 암호화
//      encryptFile(inputFile, encryptedFile, key);
//      System.out.println("파일이 암호화되어 저장되었습니다.");
//
//      // 파일 복호화
//      decryptFile(encryptedFile, decryptedFile, key);
//      System.out.println("파일이 복호화되어 저장되었습니다.");
//
//      // 파일 외 다른 형태의 데이터 처리 예시 (예: byte[]로 암호화/복호화)
//      byte[] data = "Hello, world!".getBytes(); // 바이트 배열로 입력 데이터
//      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
//
//
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//}
