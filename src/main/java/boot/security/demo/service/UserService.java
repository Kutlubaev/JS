package boot.security.demo.service;

import boot.security.demo.model.User;
import boot.security.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public void delete(User user) {
        userRepository.delete(user);
    }


}
