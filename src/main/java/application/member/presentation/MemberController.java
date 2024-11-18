package application.member.presentation;

import application.auth.Auth;
import application.auth.token.TokenResponse;
import application.auth.token.TokenService;
import application.member.application.MemberService;
import application.member.application.command.LoginCommand;
import application.member.application.command.SignupCommand;
import application.member.domain.Member;
import application.member.presentation.request.LoginRequest;
import application.member.presentation.request.SignupRequest;
import application.member.presentation.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberService memberService;
    private final MemberPresentationMapper mapper;
    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenResponse> signup(
            @RequestBody SignupRequest request
    ) {
        SignupCommand command = mapper.toCommand(request);
        Member member = memberService.signup(command);
        TokenResponse token = tokenService.createToken(member.getId());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @RequestBody LoginRequest request
    ) {
        LoginCommand command = mapper.toCommand(request);
        Member member = memberService.login(command);
        TokenResponse token = tokenService.createToken(member.getId());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/my")
    public ResponseEntity<MemberResponse> getMyInfo(
            @Auth Member member
    ) {
        return ResponseEntity.ok(MemberResponse.from(member));
    }
}
