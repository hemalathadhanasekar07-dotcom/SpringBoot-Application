package org.example.demo2.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;



@Setter
@Getter
@Entity
@Table(name="Student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String username;
    private String password;
    private String role;
    private String createdBy;
    private String modifiedBy;

    public Student() {}
    public Student(String name, String email,String username,String password,String role,String createdBy,String modifiedBy) {
        this.name = name;
        this.email = email;
        this.username=username;
        this.password=password;
        this.role=role;
        this.createdBy=createdBy;
        this.modifiedBy=modifiedBy;
    }

}