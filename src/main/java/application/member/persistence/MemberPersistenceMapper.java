package application.member.persistence;

import application.member.domain.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberPersistenceMapper {

    Member toDomain(MemberEntity entity);

    MemberEntity toEntity(Member member);
}
