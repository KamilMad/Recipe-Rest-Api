package madej.kamil.recipemanagementsystem.service;

import madej.kamil.recipemanagementsystem.model.Recipe;
import madej.kamil.recipemanagementsystem.model.User;
import madej.kamil.recipemanagementsystem.repository.RecipeRepository;
import madej.kamil.recipemanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RecipeRepository recipeRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public ResponseEntity<Long> saveUser(User user){

        if (userExistsByEmail(user.getEmail())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User newUser = createUserFromRequest(user);

        userRepository.save(newUser);

        return new ResponseEntity<>(newUser.getId(),HttpStatus.OK);

    }

    private User createUserFromRequest(User user) {
        return User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(encodePassword(user.getPassword()))
                .roles(Set.of("USER"))
                .recipes(getRecipesForUser(user))
                .build();
    }

    private List<Recipe> getRecipesForUser(User user) {
        return recipeRepository.findByUserId(user.getId());
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
    private boolean userExistsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
