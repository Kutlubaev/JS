package boot.security.demo.service;

import boot.security.demo.model.Role;
import boot.security.demo.model.User;
import boot.security.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;

    @Transactional(readOnly = true)
    public boolean isUsernameUniqueForEdit(String username, int userId) {
        Optional<User> existingUser = userRepository.findByName(username);
        return existingUser.isEmpty() || existingUser.get().getId() == (userId);
    }

    @Transactional(readOnly = true)
    public boolean isUsernameUnique(String username) {
        return userRepository.findByName(username).isEmpty();
    }

    @Transactional(readOnly = true)
    public List<User> getAllWithRoles() {
        return userRepository.findAllWithRoles();
    }

    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<User> getById(int id) {
        return userRepository.findById(id);
    }

    @Transactional
    public void add(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void edit(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public User editUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Transactional
    public User editUserWithRoles(int userId, Map<String, Object> updates) {
        // Находим пользователя по ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        // Обновляем информацию о пользователе
        updates.forEach((key, value) -> {
            switch (key) {
                case "id":
                    if (value instanceof Integer) {
                        user.setId((Integer) value);
                    } else {
                        try {
                            // Попытка преобразовать строку в Integer
                            user.setId(Integer.parseInt(value.toString()));
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Некорректный формат ID: " + value);
                        }
                    }
                    break;
                case "name":
                    user.setName((String) value);
                    break;
                case "password":
                    user.setPassword((String) value);
                    break;
                case "surname":
                    user.setSurname((String) value);
                    break;
                case "age":
                    if (value instanceof String) {
                        // Преобразование строки в Integer
                        user.setAge(Integer.parseInt((String) value));
                    } else {
                        user.setAge((Integer) value);
                    }
                    break;
                // ... обновление других полей ...
                case "roles":
                    // Обработка списка ролей
                    Set<Role> assignedRoles = new HashSet<>();
                    // Предполагаем, что value является списком объектов с полем roleName
                    List<Map<String, String>> rolesList = (List<Map<String, String>>) value;
                    rolesList.forEach(roleMap -> {
                        String roleName = roleMap.get("roleName");
                        Role role = roleService.getByName(roleName);
                        if (role == null) {
                            throw new RuntimeException("Роль с именем " + roleName + " не найдена");
                        }
                        assignedRoles.add(role);
                    });
                    user.setRoles(assignedRoles);
                    break;
                default:
                    throw new IllegalArgumentException("Неизвестное поле: " + key);
            }
        });

        // Сохраняем обновленного пользователя
        return userRepository.save(user);
    }


}
