package madej.kamil.recipemanagementsystem.payload;

import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public record UserDto(
        @Email @NotEmpty @NotNull String email,
        @NotBlank @Size(min = 8) String password
) {
}