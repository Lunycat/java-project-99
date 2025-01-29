package hexlet.code.controller.api;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<List<UserDTO>> index() {
        List<UserDTO> usersDTO = service.findAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(usersDTO.size()))
                .body(usersDTO);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO show(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@RequestBody @Valid UserCreateDTO dto) {
        return service.save(dto);
    }

    @PreAuthorize("@userUtils.isCurrentUser(#id)")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO put(@RequestBody @Valid UserUpdateDTO dto, @PathVariable Long id) {
        return service.update(dto, id);
    }

    @PreAuthorize("@userUtils.isCurrentUser(#id)")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
