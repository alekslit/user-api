package ru.telros.uapi.user;

import jakarta.persistence.*;
import lombok.*;
import ru.telros.uapi.role.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@NamedEntityGraph(name = "User.roles", attributeNodes = @NamedAttributeNode("roles"))
public class User {
    // идентификатор пользователя:
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    // имя пользователя:
    @Column(name = "first_name")
    private String firstName;

    // фамилия пользователя:
    @Column(name = "last_name")
    private String lastName;

    // отчество пользователя:
    @Column(name = "patronymic")
    private String patronymic;

    // дата рождения пользователя:
    @Column(name = "birthday")
    private LocalDate birthday;

    // электронная почта:
    @Column(name = "email")
    private String email;

    // номер телефона пользователя:
    @Column(name = "phone")
    private String phone;

    // пароль от аккаунта пользователя:
    @Column(name = "password")
    private String password;

    // роли пользователя (уровни доступа):
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    // дата и время регистрации пользователя:
    @Column(name = "registration_date")
    private LocalDateTime registrationDate;
}