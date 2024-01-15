package madej.kamil.recipemanagementsystem.controller;

import jakarta.validation.Valid;
import madej.kamil.recipemanagementsystem.model.Recipe;
import madej.kamil.recipemanagementsystem.payload.IdDto;
import madej.kamil.recipemanagementsystem.payload.RecipeDto;
import madej.kamil.recipemanagementsystem.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/new")
    public ResponseEntity<IdDto> addRecipe(@Valid @RequestBody Recipe recipe){

        return new ResponseEntity<>(new IdDto(recipeService.addRecipe(recipe))
                ,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public RecipeDto getRecipes(@PathVariable Long id){
        return recipeService.getRecipeById(id);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteRecipe(@PathVariable Long id){
        recipeService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/test")
    public String test(){
        return "Hello world";
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateRecipe(@PathVariable Long id, @Valid @RequestBody Recipe recipe){
        recipeService.updateById(id, recipe);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RecipeDto>> getAllRecipe(@RequestParam(name = "category", required = false) String category,
                                                        @RequestParam(name = "name", required = false) String name){

        List<RecipeDto> recipes = recipeService.findAll(category, name);
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

}