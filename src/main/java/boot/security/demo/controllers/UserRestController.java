package boot.security.demo.controllers;

import boot.security.demo.model.Role;
import boot.security.demo.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class UserRestController {

    @GetMapping("/user-info")
    public Map<String, Object> getUserInfo(@AuthenticationPrincipal User user) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("name", user.getName());
        userInfo.put("surname", user.getSurname());
        userInfo.put("age", user.getAge());
        userInfo.put("roles", user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()));
        return userInfo;
    }
}

