package boot.security.demo.controllers;

import boot.security.demo.model.Role;
import boot.security.demo.model.User;
import boot.security.demo.service.RoleService;
import boot.security.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/")
public class AdminRestController {

    private final UserService userService;

    @Autowired
    public AdminRestController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private RoleService roleService;

    @GetMapping("/admin")
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/user")
    public User getUserProfile(Principal principal) {
        return userService.getUserByName(principal.getName());
    }

    @GetMapping("/user/{userId}")
    public User findById(@PathVariable int userId) {
        return userService.getById(userId).orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));
    }

    @PostMapping("/add")
    public ResponseEntity<?> saveNewUser(@RequestBody @Valid User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> validationErrors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(fieldError ->
                    validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(validationErrors);
        }

        userService.saveUser(user);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<?> update(@PathVariable int userId, @RequestBody Map<String, Object> updates) {
        User updatedUser = userService.editUserWithRoles(userId, updates);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAll();
        return ResponseEntity.ok(roles);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "id") int userId) {
        return userService.getById(userId)
                .map(user -> {
                    userService.delete(user);
                    return ResponseEntity.ok().build();
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{userId}/roles")
    public ResponseEntity<List<String>> getUserRoles(@PathVariable int userId) {
        try {
            User user = userService.getById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));
            List<String> roles = user.getRoles().stream()
                    .map(Role::getRoleName)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(roles, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
