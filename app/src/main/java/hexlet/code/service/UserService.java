package hexlet.code.service;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;

    private final UserMapper mapper;

    public List<UserDTO> findAll() {
        List<User> users = repository.findAll();
        return mapper.toListUserDTO(users);
    }

    public UserDTO findById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found user with id = " + id));
        return mapper.toUserDTO(user);
    }

    public UserDTO save(UserCreateDTO dto) {
        User user = mapper.toUser(dto);
        repository.save(user);
        return mapper.toUserDTO(user);
    }

    public UserDTO update(UserUpdateDTO dto, Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found user with id = " + id));
        mapper.update(dto, user);
        repository.save(user);
        return mapper.toUserDTO(user);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
