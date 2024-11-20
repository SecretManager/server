package application.member.presentation.response;

import application.member.domain.Member;
import application.member.domain.MembershipType;

public record MemberResponse(
        Long id,
        String email,
        MembershipType membershipType,
        long currentSavedFileBytes
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getMembership().getMembershipType(),
                member.getMembership().getCurrentSavedFileBytes()
        );
    }
}
