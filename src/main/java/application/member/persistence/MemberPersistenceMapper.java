package application.member.persistence;

import application.member.domain.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberPersistenceMapper {

    @Mapping(source = "membershipType", target = "membership.membershipType")
    @Mapping(source = "currentSavedFileBytes", target = "membership.currentSavedFileBytes")
    Member toDomain(MemberEntity entity);

    @Mapping(source = "membership.membershipType", target = "membershipType")
    @Mapping(source = "membership.currentSavedFileBytes", target = "currentSavedFileBytes")
    MemberEntity toEntity(Member member);
}
