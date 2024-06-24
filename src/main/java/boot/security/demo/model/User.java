package boot.security.demo.model;



import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
@Table(name= "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "name should be not empty")
    @Pattern(regexp = "[а-яА-ЯёЁa-zA-Z]+", message = "Имя должно содержать только буквы")
    @Size(min = 2 , max = 30)
    private String name;

    @Column(name = "surname")
    @NotEmpty(message = "surname should be not empty")
    @Pattern(regexp = "[а-яА-ЯёЁa-zA-Z]+", message = "Фамилия должна содержать только буквы")
    @Size(min = 2 , max = 30)
    private String surname;

    @Column(name = "age")
    @Min(value = 0, message = "Age should be greater than 0")
    private int age;

    @Column(name="password")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public @NotEmpty(message = "name should be not empty") @Pattern(regexp = "[а-яА-ЯёЁa-zA-Z]+", message = "Имя должно содержать только буквы") @Size(min = 2, max = 30) String getName() {
        return name;
    }

    public void setName(@NotEmpty(message = "name should be not empty") @Pattern(regexp = "[а-яА-ЯёЁa-zA-Z]+", message = "Имя должно содержать только буквы") @Size(min = 2, max = 30) String name) {
        this.name = name;
    }

    public @NotEmpty(message = "surname should be not empty") @Pattern(regexp = "[а-яА-ЯёЁa-zA-Z]+", message = "Фамилия должна содержать только буквы") @Size(min = 2, max = 30) String getSurname() {
        return surname;
    }

    public void setSurname(@NotEmpty(message = "surname should be not empty") @Pattern(regexp = "[а-яА-ЯёЁa-zA-Z]+", message = "Фамилия должна содержать только буквы") @Size(min = 2, max = 30) String surname) {
        this.surname = surname;
    }

    @Min(value = 0, message = "Age should be greater than 0")
    public int getAge() {
        return age;
    }

    public void setAge(@Min(value = 0, message = "Age should be greater than 0") int age) {
        this.age = age;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList());
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}