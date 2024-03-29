package madej.kamil.recipemanagementsystem.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @NotBlank
    private String name;

    @Column
    @NotBlank
    private String category;
    @Column
    @NotBlank
    private String description;

    @Column
    @UpdateTimestamp
    private LocalDateTime date;

    @ElementCollection
    @CollectionTable(name = "ingredients", joinColumns = @JoinColumn(name = "recipe"))
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @NotEmpty
    @Size(min = 1)
    private List<String> ingredients;

    @ElementCollection
    @CollectionTable(name = "directions", joinColumns = @JoinColumn(name = "recipe"))
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @NotEmpty
    @Size(min = 1)
    private List<String> directions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Recipe(String name, String category, String description, List<String> ingredients, List<String> directions) {
        this.name = name;
        this.category = category;
        this.date = LocalDateTime.now();
        this.description = description;
        this.ingredients = ingredients;
        this.directions = directions;
    }
}