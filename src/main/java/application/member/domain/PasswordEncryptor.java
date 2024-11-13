package application.member.domain;

import application.encrypt.algorithm.Bcrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncryptor {

    public String encrypt(String plainPassword) {
        return Bcrypt.encrypt(plainPassword);
    }

    public boolean checkPassword(String plainPassword, String hashedPassword) {
        return Bcrypt.check(plainPassword, hashedPassword);
    }
}
