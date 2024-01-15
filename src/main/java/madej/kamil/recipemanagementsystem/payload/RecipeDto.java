package madej.kamil.recipemanagementsystem.payload;

import java.time.LocalDateTime;
import java.util.List;

public record RecipeDto(
        String name,
        String category,
        LocalDateTime date,
        String description,
        List<String> ingredients,
        List<String> directions
) {
}