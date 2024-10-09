package com.learning.demo.student;

import com.learning.demo.exception.StudentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {

    private final StudentService studentService;

    @Autowired //dependency injection
    public StudentController(StudentService studentService) {
        this.studentService = studentService ;
    }

    @GetMapping
    public List<Student> getAllStudents() {
        try {
            return studentService.getAllStudents();
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    @GetMapping("/get/{studentId}")
    public Student getStudent(@PathVariable Long studentId) {
        return studentService.getStudentById(studentId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerNewStudent(@RequestBody Student student){
        studentService.registerNewStudent(student);
    }

    @DeleteMapping(path = "/deleteById/{studentId}")
    public void  deleteStudentById(@PathVariable("studentId") Long studentId){
        studentService.deleteStudentById(studentId);
    }

    @DeleteMapping(path = "/deleteByEmail/{studentEmail}")
    public void  deleteStudentByEmail(@PathVariable("studentEmail") String studentEmail){
        studentService.deleteStudentByEmail(studentEmail);
    }


    @PutMapping(path = "/updateById/{studentId}")
    public void updateStudent(
            @PathVariable("studentId") Long studentId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {
        studentService.updateStudentById(studentId, name, email);
    }

    @PutMapping(path = "/updateByEmail/{studentEmail}")
    public void updateStudent(
            @PathVariable("studentEmail") String studentEmail,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {
        studentService.updateStudentByEmail(studentEmail,name, email);
    }

    @GetMapping("/getStudentsByMailProvider/{emailProvider}")
    public List<Student> getStudentsByMailProvider(@PathVariable String emailProvider){
        try {
            return studentService.getAllStudentsByMailProvider(emailProvider);
        } catch (StudentNotFoundException e) {
            throw new StudentNotFoundException(e.getMessage());
        } /* catch (Exception e){
            throw new IllegalStateException();
        }
        */
    }

}
