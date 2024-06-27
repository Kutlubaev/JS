package boot.security.demo.service;

import boot.security.demo.model.Role;
import boot.security.demo.model.User;
import boot.security.demo.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Role getById(int id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role with id " + id + " not found"));
    }


    public Role getByName(String roleName){
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("Role with id " + roleName + " not found"));
    }

    @Transactional
    public void add(Role role) {
        roleRepository.save(role);
    }

    @Transactional
    public void edit(Role role) {
        roleRepository.save(role);
    }

    @Transactional
    public void delete(Role role) {
        roleRepository.delete(role);
    }
}
