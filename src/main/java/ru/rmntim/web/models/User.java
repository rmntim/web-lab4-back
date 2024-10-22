package ru.rmntim.web.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", schema = "public")
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password_hash_b64")
    private String passwordHashB64;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final Set<Point> points = new HashSet<>();
}
