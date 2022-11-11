package ru.skypro.homework.mappers;

import org.mapstruct.*;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.model.Users;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    UserDto userToUserDto(Users user);

    Users userDtoToUser (UserDto userDto);

    @Mapping(target = "email", source = "username")
    Users registerReqDtoToUser(RegisterReqDto registerReqDto);

    List<UserDto> listUsersToListUserDto(List<Users> usersList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromUserDto(UserDto userDto, @MappingTarget Users user);

    @Mapping(target = "password",source = "newPassword")
    void updatePassword(NewPasswordDto newPasswordDto, @MappingTarget Users user);
}
