package com.learning.demo.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface StudentRepository
        extends JpaRepository<Student, Long> { //Long = the type of the student.i

//    @Param is not always necessary
//    It is mostly a matter of preference or clarity

    @Query("SELECT s FROM Student s WHERE s.email = ?1")
    Optional<Student> findStudentByEmail(@Param("email")String email);

    @Query("SELECT s FROM Student s WHERE s.email LIKE %:email%")
    List<Student> findStudentByEmailContaining(@Param("email")String email);

}
