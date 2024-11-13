package application.member.domain;

import application.common.ApplicationException;
import application.common.Default;
import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
public class Member {

    private final Long id;
    private final String username;
    private String plainPassword;
    private String hashedPassword;

    @Nullable
    private String email;

    public Member(String username, String plainPassword, @Nullable String email) {
        this.id = null;
        this.username = username;
        this.plainPassword = plainPassword;
        this.email = email;
    }

    @Default
    public Member(Long id, String username, String hashedPassword, String email) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.email = email;
    }

    public void signup(MemberValidator validator, PasswordEncryptor encryptor) {
        validator.validateSignup(username);
        this.hashedPassword = encryptor.encrypt(plainPassword);
        this.plainPassword = null;
    }

    public void login(PasswordEncryptor encryptor, String plainPassword) {
        boolean pass = encryptor.checkPassword(plainPassword, this.hashedPassword);
        if (!pass) {
            throw new ApplicationException("로그인에 실패했습니다. 비밀번호를 확인해주세요.");
        }
    }
}
