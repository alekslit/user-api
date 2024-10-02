package ru.telros.uapi.role;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles", schema = "public")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    // идентификатор:
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer id;

    // название роли:
    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private RoleName name;
}