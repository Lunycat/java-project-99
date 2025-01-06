package hexlet.code.service;

import hexlet.code.dto.userDTO.UserCreateDTO;
import hexlet.code.dto.userDTO.UserDTO;
import hexlet.code.dto.userDTO.UserUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserMapper mapper;

    public List<UserDTO> getAll() {
        List<User> users = repository.findAll();
        return mapper.toListUserDTO(users);
    }

    public UserDTO findById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found user with id = " + id));
        return mapper.toUserDTO(user);
    }

    public UserDTO create(UserCreateDTO dto) {
        User user = mapper.toUser(dto);
        repository.save(user);
        return mapper.toUserDTO(user);
    }

    public UserDTO update(UserUpdateDTO dto, Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found user with id = " + id));
        mapper.update(dto, user);
        return mapper.toUserDTO(user);
    }

    public void destroy(Long id) {
        repository.deleteById(id);
    }
}
