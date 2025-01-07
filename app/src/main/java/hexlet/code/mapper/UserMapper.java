package hexlet.code.mapper;

import hexlet.code.dto.userDTO.UserCreateDTO;
import hexlet.code.dto.userDTO.UserDTO;
import hexlet.code.dto.userDTO.UserUpdateDTO;
import hexlet.code.model.User;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
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

    public abstract UserDTO toUserDTO(User model);
    public abstract User toUser(UserCreateDTO dto);
    public abstract User toUser(UserDTO dto);
    public abstract void update(UserUpdateDTO dto, @MappingTarget User destination);
    public abstract List<UserDTO> toListUserDTO(List<User> models);
    public abstract List<User> toListUser(List<UserDTO> dtoList);
}
