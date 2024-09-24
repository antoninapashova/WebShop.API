package com.example.webshopapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
