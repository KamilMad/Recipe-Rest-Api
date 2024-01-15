package madej.kamil.recipemanagementsystem.service;

import madej.kamil.recipemanagementsystem.errors.RecipeNotFoundException;
import madej.kamil.recipemanagementsystem.model.Recipe;
import madej.kamil.recipemanagementsystem.model.User;
import madej.kamil.recipemanagementsystem.payload.RecipeDto;
import madej.kamil.recipemanagementsystem.repository.RecipeRepository;
import madej.kamil.recipemanagementsystem.repository.UserRepository;
import madej.kamil.recipemanagementsystem.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RecipeService(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    public Long addRecipe(Recipe recipe) {

        Recipe newRecipe = createRecipe(recipe);
        recipeRepository.save(newRecipe);

        User user = getCurrentUser();
        user.getRecipes().add(newRecipe);

        return newRecipe.getId();
    }

    public RecipeDto getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("No such a recipe"));

        return fromDto(recipe);
    }

    public void deleteById(Long id) {

        Recipe recipe = recipeRepository.findById(id).orElseThrow(
                () -> new RecipeNotFoundException("No such a recipe"));

        if (isNotAuthor(recipe)) {
            throw new AccessDeniedException("Access denied");
        }

        recipeRepository.deleteById(id);
    }

    public void updateById(Long id, Recipe recipe){

        Recipe existingRecipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("No such a recipe"));

        validateAuthorship(recipe);

        updateRecipeDetails(existingRecipe, recipe);

        recipeRepository.save(existingRecipe);
    }

    public List<RecipeDto> findAll(String category, String name){

        if (category == null && name == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        }

        List<Recipe> recipes;

        if (category != null){
            recipes = recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category);
        }
        else{
            recipes = recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(name);
        }

        return recipes.stream()
                .map(this::fromDto)
                .collect(Collectors.toList());
    }

    private RecipeDto fromDto(Recipe recipe){

        return new RecipeDto(recipe.getName(),
                recipe.getCategory(),
                recipe.getDate(),
                recipe.getDescription(),
                recipe.getIngredients(),
                recipe.getDirections());
    }

    private Recipe createRecipe(Recipe recipe) {
        User user = getCurrentUser();

        Recipe newRecipe = new Recipe();
        newRecipe.setName(recipe.getName());
        newRecipe.setCategory(recipe.getCategory());
        newRecipe.setDescription(recipe.getDescription());
        newRecipe.setIngredients(recipe.getIngredients());
        newRecipe.setDirections(recipe.getDirections());
        newRecipe.setUser(user);

        return newRecipe;
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Such a user not found"));
    }

    public boolean isNotAuthor(Recipe recipe) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        return (recipe.getUser().getId() != userDetails.getUser().getId());
    }

    private void validateAuthorship(Recipe recipe) {
        if (isNotAuthor(recipe)) {
            throw new AccessDeniedException("Access denied");
        }
    }

    private void updateRecipeDetails(Recipe existingRecipe, Recipe updatedRecipe) {
        existingRecipe.setName(updatedRecipe.getName());
        existingRecipe.setDate(LocalDateTime.now());
        existingRecipe.setIngredients(updatedRecipe.getIngredients());
        existingRecipe.setDescription(updatedRecipe.getDescription());
        existingRecipe.setCategory(updatedRecipe.getCategory());
        existingRecipe.setDirections(updatedRecipe.getDirections());
    }
}