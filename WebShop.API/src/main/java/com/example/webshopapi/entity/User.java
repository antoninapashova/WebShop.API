package com.example.webshopapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UserRole role;
}
