package application.common;

import org.springframework.web.multipart.MultipartFile;

public class FileUtils {

    public static String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            return ""; // 확장자가 없는 경우 빈 문자열 반환
        }
        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    }
}
