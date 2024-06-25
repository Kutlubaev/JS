package boot.security.demo.repositories;

import boot.security.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.name = :name")
    Optional<User> findByName(String name);


    @Query("SELECT DISTINCT u FROM User u JOIN FETCH u.roles")
    List<User> findAllWithRoles();
}
