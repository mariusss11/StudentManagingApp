package com.learning.demo.student;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Table
@Getter
@Setter
@ToString
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Use IDENTITY for auto-increment
    private Long id;

    private String name;
    private String email;
    private LocalDate dob;

    @Transient
    private Integer age; // No need to store it in the database

    public Student() {}

    public Student(Long id, String name, String email, LocalDate dob) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dob = dob;
    }

    public Student(String name, String email, LocalDate dob) {
        this.name = name;
        this.email = email;
        this.dob = dob;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Integer getAge() {
        return Period.between(this.dob, LocalDate.now()).getYears();
    }

//    public void setAge(Integer age) {
//        this.age = age;
//    }

    public void validateName() {
        if (this.name == null || this.name.isBlank() || !this.name.matches("[a-zA-Z\\s]+")) {
            throw new RuntimeException("Invalid Name");
        }
    }

    public void validateEmail() {
        if (this.email == null || this.email.isBlank()) {
            throw new RuntimeException("Invalid Email");
        }
    }

    public void validateDob(){
        if (this.dob == null) {
            throw new RuntimeException("Date of Birth cannot be null");
        }
        if (this.dob.isAfter(LocalDate.now()))
            throw new RuntimeException("Invalid Date");
    }
}
