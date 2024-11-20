package application.member.presentation;

import application.auth.Auth;
import application.auth.token.TokenResponse;
import application.auth.token.TokenService;
import application.member.application.MemberQueryService;
import application.member.application.MemberService;
import application.member.application.command.LoginCommand;
import application.member.application.command.SignupCommand;
import application.member.domain.Member;
import application.member.domain.MemberRepository;
import application.member.presentation.request.LoginRequest;
import application.member.presentation.request.SignupRequest;
import application.member.presentation.request.UpdateProfileRequest;
import application.member.presentation.response.MemberResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberService memberService;
    private final MemberQueryService memberQueryService;
    private final MemberPresentationMapper mapper;
    private final TokenService tokenService;
    private final MemberRepository memberRepository;

    @GetMapping("/check-duplicate-username")
    public ResponseEntity<Boolean> checkDuplicatedUsername(
            @RequestParam("username") String username
    ) {
        return ResponseEntity.ok(memberRepository.existsByUsername(username));
    }

    @PostMapping
    public ResponseEntity<TokenResponse> signup(
            @RequestBody @Valid SignupRequest request
    ) {
        SignupCommand command = mapper.toCommand(request);
        Member member = memberService.signup(command);
        TokenResponse token = tokenService.createToken(member.getId());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @RequestBody @Valid LoginRequest request
    ) {
        LoginCommand command = mapper.toCommand(request);
        Member member = memberService.login(command);
        TokenResponse token = tokenService.createToken(member.getId());
        return ResponseEntity.ok(token);
    }

    @PutMapping
    public void updateProfile(
            @Auth Member member,
            @RequestBody @Valid UpdateProfileRequest request
    ) {
        memberService.update(request.toCommand(member));
    }

    @GetMapping("/my")
    public ResponseEntity<MemberResponse> getMyInfo(
            @Auth Member member
    ) {
        return ResponseEntity.ok(MemberResponse.from(member));
    }
}
