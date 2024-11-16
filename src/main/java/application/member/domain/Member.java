package application.member.domain;

import application.common.Default;
import application.common.algorithm.Bcrypt;
import application.common.exception.UnAuthorizedException;
import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
public class Member {

    private final Long id;
    private final String username;
    private String hashedPassword;

    @Nullable
    private String email;

    public static Member preSignup(String username, String plainPassword, @Nullable String email) {
        return new Member(
                username,
                Bcrypt.encrypt(plainPassword),
                email
        );
    }

    private Member(String username, String hashedPassword, @Nullable String email) {
        this.id = null;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.email = email;
    }

    @Default
    public Member(Long id, String username, String hashedPassword, String email) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.email = email;
    }

    public void signup(MemberValidator validator) {
        validator.validateSignup(username);
    }

    public void login(String plainPassword) {
        boolean pass = Bcrypt.check(plainPassword, hashedPassword);
        if (!pass) {
            throw new UnAuthorizedException("로그인에 실패했습니다. 비밀번호를 확인해주세요.");
        }
    }
}
