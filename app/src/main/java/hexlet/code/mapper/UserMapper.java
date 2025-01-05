package hexlet.code.mapper;

import hexlet.code.dto.userDTO.CreateOrUpdateUserDTO;
import hexlet.code.dto.userDTO.UserDTO;
import hexlet.code.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    public abstract UserDTO toUserDTO(User model);
    public abstract User toUser(CreateOrUpdateUserDTO dto);
    public abstract
    public abstract List<UserDTO> toListUserDTO(List<User> models);
}
