package com.learning.demo.student;

import com.learning.demo.exception.DuplicateEmailException;
import com.learning.demo.exception.InvalidDataException;
import com.learning.demo.exception.SameEmailUpdateException;
import com.learning.demo.exception.StudentNotFoundException;
import com.sun.jdi.request.InvalidRequestStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service //substitutes Component
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Requested Student does not exist"));
    }

    public List<Student> getAllStudentsByMailProvider(String emailProvider){
        List<Student> studentList = studentRepository.findStudentByEmailContaining(emailProvider);
        if (studentList.isEmpty())
            throw new StudentNotFoundException("No students found with the email provider " + emailProvider);
        return studentList;
    }

    public void addNewStudent(String name, String email, LocalDate dob) throws InvalidRequestStateException {
        Optional<Student> studentOptional = studentRepository
                .findStudentByEmail(email);
        validateName(name);
        validateDob(dob);
        validateEmail(email);
        if (studentOptional.isPresent()) {
            throw new DuplicateEmailException("Email is already taken.");
        }
        Student newStudent = new Student(
                name, email, dob
        );
        studentRepository.save(newStudent);
    }

    public void registerNewStudent(Student student) throws InvalidRequestStateException {
        validateStudentData(student);
        Optional<Student> studentOptional = studentRepository
                .findStudentByEmail(student.getEmail());
        if (studentOptional.isPresent()) {
            throw new DuplicateEmailException("Email is already taken.");
        }
        studentRepository.save(student);
    }

    public void deleteStudentById(Long studentId) {
            if (!studentRepository.existsById(studentId)){
                throw new StudentNotFoundException("Student with ID "+ studentId + " does not exist.");
            }
            studentRepository.deleteById(studentId);
    }

    public void deleteStudentByEmail(String studentEmail){
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(studentEmail);
        if (studentOptional.isEmpty()){
            throw new StudentNotFoundException("Student with email " + studentEmail + " does not exists");
        }
        Student studentToDelete = studentOptional.get();
        studentRepository.deleteById(studentToDelete.getId());
    }

    @Transactional
    public void updateStudentById(Long studentId, String name, String email) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(
                        "student with id " + studentId + " does not exists"));

        // Validate the input data
        if (name != null)
            updateStudentName(student.getEmail(), name);
        if (email != null)
            updateStudentEmail(student.getEmail(), email);
    }

    @Transactional
    public void updateStudentByEmail(
            String studentEmail,
            String name,
            String email) {
        Student student = studentRepository.findStudentByEmail(studentEmail)
                .orElseThrow(() -> new StudentNotFoundException(
                        "student with email " + studentEmail + " does not exists"));
        // Validate the input data
        if (name != null)
            updateStudentName(student.getEmail(), name);
        if (email != null)
            updateStudentEmail(student.getEmail(), email);
    }

    public void updateStudentName(String email, String updatedName){
        validateName(updatedName);
        Optional<Student> optionalStudent = studentRepository.findStudentByEmail(email);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            student.setName(updatedName);
            studentRepository.save(student);
        } else {
            throw new RuntimeException("Student with email " + email + " not found");
        }
    }

    public void updateStudentEmail(String initialEmail, String updatedEmail){
        Optional<Student> optionalStudent = studentRepository.findStudentByEmail(initialEmail);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            validateEmail(updatedEmail, student);
            student.setEmail(updatedEmail);
            studentRepository.save(student);
        } else {
            throw new RuntimeException("Student with email " + initialEmail + " not found");
        }
    }

    // Method to validate student data
    private void validateStudentData(Student student) {
        try {
            student.validateName(); // Validate name
            student.validateEmail(); // Validate email
            student.validateDob(); // Validate date of birth
        } catch (InvalidDataException e) {
            throw new InvalidRequestStateException("Invalid student data: " + e.getMessage());
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank() || !name.matches("[a-zA-Z\\s]+")) {
            throw new InvalidDataException("Invalid Name");
        }
    }

    private void validateEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new InvalidDataException("Invalid Email");
        }
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
        if (studentOptional.isPresent()) {
            throw new DuplicateEmailException("Email " + email + " is already in use by another student.");
        }
    }

    private void validateEmail(String email, Student student) {
        if (email == null || !email.contains("@")) {
            throw new InvalidDataException("Invalid Email");
        }
        if (Objects.equals(student.getEmail(), email)) {
            throw new SameEmailUpdateException("The email is already the same as the current email.");
        }
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
        if (studentOptional.isPresent()) {
            throw new DuplicateEmailException("Email " + email + " is already in use by another student.");
        }
    }

    private void validateDob(LocalDate dob) {
        if (dob == null ||dob.isAfter(LocalDate.now())) {
            throw new InvalidDataException("Invalid Date");
        }

    }
}


