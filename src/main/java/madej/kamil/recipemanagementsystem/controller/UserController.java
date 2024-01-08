package madej.kamil.recipemanagementsystem.controller;

import jakarta.validation.Valid;
import madej.kamil.recipemanagementsystem.model.User;
import madej.kamil.recipemanagementsystem.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Long> addUser(@Valid @RequestBody User user){
        return userService.saveUser(user);
    }
}
