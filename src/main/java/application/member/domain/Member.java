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

    private String email;

    private Membership membership;

    public static Member preSignup(String username, String plainPassword, @Nullable String email) {
        return new Member(
                null,
                username,
                Bcrypt.encrypt(plainPassword),
                email,
                new Membership(MembershipType.FREE)
        );
    }

    @Default
    public Member(Long id, String username, String hashedPassword, String email, Membership membership) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.email = email;
        this.membership = membership;
    }

    public void signup(MemberValidator validator) {
        validator.validateSignup(this);
    }

    public void login(String plainPassword) {
        boolean pass = Bcrypt.check(plainPassword, hashedPassword);
        if (!pass) {
            throw new UnAuthorizedException("로그인에 실패했습니다. 비밀번호를 확인해주세요.");
        }
    }

    public void uploadFile(long fileBytes) {
        membership.uploadFile(fileBytes);
    }

    public void downloadFile(int currentDownloadCountPerMonth) {
        membership.downloadFile(currentDownloadCountPerMonth);
    }
}
