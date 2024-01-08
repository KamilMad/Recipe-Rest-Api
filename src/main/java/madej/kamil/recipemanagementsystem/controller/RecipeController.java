package madej.kamil.recipemanagementsystem.controller;

import jakarta.validation.Valid;
import madej.kamil.recipemanagementsystem.model.Recipe;
import madej.kamil.recipemanagementsystem.payload.IdDto;
import madej.kamil.recipemanagementsystem.payload.RecipeDto;
import madej.kamil.recipemanagementsystem.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/recipe/new")
    public ResponseEntity<IdDto> addRecipe(@Valid @RequestBody Recipe recipe){

        return new ResponseEntity<>(new IdDto(recipeService.addRecipe(recipe))
                ,HttpStatus.OK);
    }

    @GetMapping("/recipe/{id}")
    public RecipeDto getRecipes(@PathVariable Long id){
        return recipeService.getRecipeById(id);
    }


    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<HttpStatus> deleteRecipe(@PathVariable Long id){
        recipeService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/recipe/test")
    public String test(){
        return "Hello world";
    }

    @PutMapping("/recipe/{id}")
    public ResponseEntity<HttpStatus> updateRecipe(@PathVariable Long id, @Valid @RequestBody Recipe recipe){
        recipeService.updateById(id, recipe);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/recipe/search")
    public ResponseEntity<List<RecipeDto>> getAllRecipe(@RequestParam(name = "category", required = false) String category,
                                                        @RequestParam(name = "name", required = false) String name){

        List<RecipeDto> recipes = recipeService.findAll(category, name);
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

}