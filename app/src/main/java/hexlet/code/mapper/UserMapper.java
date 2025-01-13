package hexlet.code.mapper;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.model.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.BeforeMapping;

import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Mapper(
        uses = {JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {

    @Autowired
    private PasswordEncoder encoder;

    @BeforeMapping
    public void encodePassword(UserCreateDTO dto) {
        String password = dto.getPassword();
        dto.setPassword(encoder.encode(password));
    }

    @BeforeMapping
    public void encodePassword(UserUpdateDTO dto) {
        String password = dto.getPassword().get();
        dto.setPassword(JsonNullable.of(encoder.encode(password)));
    }

    @Mapping(target = "passwordDigest", source = "password")
    public abstract User toUser(UserCreateDTO dto);

    @Mapping(source = "password", target = "passwordDigest")
    public abstract void update(UserUpdateDTO dto, @MappingTarget User destination);

    public abstract UserDTO toUserDTO(User model);
    public abstract User toUser(UserDTO dto);
    public abstract List<UserDTO> toListUserDTO(List<User> models);
    public abstract List<User> toListUser(List<UserDTO> dtoList);
}
