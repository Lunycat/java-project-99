package hexlet.code.dto.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class UserUpdateDTO {

    private JsonNullable<String> firstName;

    private JsonNullable<String> lastName;

    @NotNull
    private JsonNullable<String> email;

    @NotNull
    @Min(value = 3)
    private JsonNullable<String> password;
}
