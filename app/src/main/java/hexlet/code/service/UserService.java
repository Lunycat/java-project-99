package hexlet.code.service;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
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

    public List<UserDTO> findAll() {
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

    public UserDTO put(UserUpdateDTO dto, Long id) {
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
