package madej.kamil.recipemanagementsystem.payload;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    @Email
    @NotEmpty
    @NotNull
    private String email;

    @Column
    @NotBlank
    @Size(min = 8)
    private String password;
}