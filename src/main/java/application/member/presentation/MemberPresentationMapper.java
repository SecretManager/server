package application.member.presentation;

import application.member.application.command.LoginCommand;
import application.member.application.command.SignupCommand;
import application.member.presentation.request.LoginRequest;
import application.member.presentation.request.SignupRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberPresentationMapper {

    SignupCommand toCommand(SignupRequest request);

    LoginCommand toCommand(LoginRequest request);
}
