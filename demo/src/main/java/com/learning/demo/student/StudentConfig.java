package com.learning.demo.student;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

import static java.time.Month.JANUARY;

@Configuration
public class StudentConfig {

    private void saveStudentIfNotExists(StudentRepository repository, Student student) {
        if (repository.findStudentByEmail(student.getEmail()).isEmpty()) {
            repository.save(student);
        }
    }

//    @Bean
//    CommandLineRunner commandLineRunner(StudentRepository repository) {
//        return args -> {
//            saveStudentIfNotExists(repository, new Student(
//                    "Mariam",
//                    "mariam.jamal@gmail.com",
//                    LocalDate.of(2000, JANUARY, 5)
//            ));
//            saveStudentIfNotExists(repository, new Student(
//                    "Alex",
//                    "alex@gmail.com",
//                    LocalDate.of(2004, JANUARY, 5)
//            ));
//        };
//    }
}

