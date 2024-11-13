package application.auth;

import application.auth.token.TokenService;
import application.member.domain.Member;
import application.member.domain.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
@RequiredArgsConstructor
public class OptionalAuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;
    private final TokenService tokenService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(OptionalAuth.class)
               && parameter.getParameterType().equals(Optional.class)
               && parameter.getGenericParameterType().equals(Member.class);
    }

    @Override
    public Optional<Member> resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = extractAccessToken(request);
        try {
            Long memberId = tokenService.extractMemberId(token);
            return Optional.of(memberRepository.getById(memberId));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static String extractAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
