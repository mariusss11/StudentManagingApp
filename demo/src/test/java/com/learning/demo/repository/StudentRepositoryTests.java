package com.learning.demo.repository;

import com.learning.demo.exception.DuplicateEmailException;
import com.learning.demo.exception.InvalidDataException;
import com.learning.demo.exception.SameEmailUpdateException;
import com.learning.demo.student.Student;
import com.learning.demo.student.StudentRepository;
import com.learning.demo.student.StudentService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StudentRepositoryTests {

    // given - when - then

//    Removed the annotation because of the constructor
//    @Autowired
    private final StudentRepository studentRepository;
    private final StudentService studentService;

    @Autowired
    public StudentRepositoryTests(StudentRepository studentRepository, StudentService studentService) {
        this.studentRepository = studentRepository;
        this.studentService = studentService;
    }

    private final String VALID_NAME_EXAMPLE = "Student Name";
    private final String VALID_EMAIL_EXAMPLE = "student.name123@example.org";

    @BeforeEach
    void setUp(){
        System.out.println("Print before each test");
    }

    @AfterEach
    void tearDown(){
        studentRepository.deleteAll();
    }

    @Test
    @DisplayName("Should Create Contact")
    public void shouldCreateContact(){
        Student student = new Student(
                VALID_NAME_EXAMPLE,
                VALID_EMAIL_EXAMPLE,
                LocalDate.of(2007, 9, 11)
        );
        studentService.registerNewStudent(student);
        assertFalse(studentRepository.findAll().isEmpty()); //verify the list is not empty
        assertEquals(1,studentRepository.findAll().size()); //verify is just one student
        Assertions.assertTrue(studentService.getAllStudents().stream()
                .anyMatch(student1 -> student.getName().equals(VALID_NAME_EXAMPLE) &&
                        student.getEmail().equals(VALID_EMAIL_EXAMPLE) &&
                        student.getDob().equals(LocalDate.of(2007, 9, 11))));
    }

//    Name Tests
    @Nested
    public class NameTests{

        @Test
        @DisplayName("Should Not Create When Name Is Null")
        public void shouldThrowInvalidDataExceptionWhenNameIsNull () {
        Assertions.assertThrows(InvalidDataException.class, () ->
                studentService.addNewStudent(
                        null,
                        VALID_EMAIL_EXAMPLE,
                        LocalDate.of(2007, 9, 11)));
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "example123"})
        @DisplayName("Should Not Create When Name Is Invalid")
        public void shouldThrowInvalidDataExceptionForInvalidName (String name){
        Assertions.assertThrows(InvalidDataException.class, () ->
                studentService.addNewStudent(name, VALID_EMAIL_EXAMPLE,
                        LocalDate.now()));
        }
    }

//    Email Tests
    @Nested
    public class EmailTests {

        @Test
        @DisplayName("Should Not Create When Email Is Null")
        public void shouldThrowInvalidDataExceptionWhenEmailIsNull() {
            Assertions.assertThrows(InvalidDataException.class, () ->
                    studentService.addNewStudent(
                            VALID_NAME_EXAMPLE,
                            null,
                            LocalDate.of(2007, 9, 11)));
        }

        @Test
        @DisplayName("Should Not Create When Email Is Empty")
        public void shouldThrowInvalidDataExceptionWhenEmailIsEmpty() {
            Assertions.assertThrows(InvalidDataException.class, () ->
                    studentService.addNewStudent(
                            VALID_NAME_EXAMPLE,
                            "",
                            LocalDate.of(2007, 9, 11)));
        }

        @Test
        @DisplayName("Search Email FOUND")
        void shouldFindStudent() {
            studentService.addNewStudent(
                    VALID_NAME_EXAMPLE,
                    VALID_EMAIL_EXAMPLE,
                    LocalDate.now());
            assertTrue(studentRepository.
                    findStudentByEmail(VALID_EMAIL_EXAMPLE).isPresent());
        }

        @Test
        @DisplayName("Search Email NOT FOUND")
        void shouldNOTFindStudent() {
            assertFalse(studentRepository.
                    findStudentByEmail("nonExistent@gmail.com").
                    isPresent());
        }

        @Test
        @DisplayName("Search Students By Partial Email")
        public void shouldFindEmailByPartialEmail() {
            studentService.addNewStudent("firstStudent", "student1@gmail.com", LocalDate.now());
            studentService.addNewStudent("secondStudent", "student2@gmail.com", LocalDate.now());

            List<Student> gmailUsers = studentRepository.findStudentByEmailContaining("@gmail.com");
            assertEquals(2, gmailUsers.size());
        }

        @Test
        @DisplayName("Should Not Create a Student with Duplicate Email")
        public void shouldThrowDuplicateEmailExceptionWhenEmailIsDuplicate() {
            String duplicateEmail = "duplicate@gmail.com";
            studentService.addNewStudent(VALID_NAME_EXAMPLE, duplicateEmail,
                    LocalDate.of(2007, 9, 11));

            Assertions.assertThrows(DuplicateEmailException.class, () ->
                    studentService.addNewStudent(VALID_NAME_EXAMPLE,
                            duplicateEmail,
                            LocalDate.of(2007, 9, 11)));
        }

        @Test
        @DisplayName("Should NOT Update If Email Is The Same")
        public void shouldNotUpdateEmailIfSame() {
            String duplicateEmail = "duplicateEmail@gmail.com";
            studentService.addNewStudent("Student", duplicateEmail, LocalDate.now());
            assertThrows(SameEmailUpdateException.class,
                    () -> studentService.updateStudentEmail(duplicateEmail, duplicateEmail));
        }
    }

//    Date Tests
    @Nested
    public class DateTests {

        @Test
        @DisplayName("Should Not Create When Date Is Null")
        public void shouldThrowRuntimeExceptionWhenDateIsNull() {
            Assertions.assertThrows(InvalidDataException.class, () ->
                    studentService.addNewStudent(
                            VALID_NAME_EXAMPLE,
                            VALID_EMAIL_EXAMPLE,
                            null));
        }

        @Test
        @DisplayName("Should Not Create When Date Is Invalid")
        public void shouldThrowRuntimeExceptionWhenDateIsInvalid() {
            LocalDate invalidDate = LocalDate.now().plusDays(1);
            Assertions.assertThrows(InvalidDataException.class, () ->
                    studentService.addNewStudent(
                            VALID_NAME_EXAMPLE,
                            VALID_EMAIL_EXAMPLE,
                            invalidDate));
        }

        @Test
        @DisplayName("Should Calculate Age")
        public void shouldCorrectlyCalculateAge() {
            LocalDate dob = LocalDate.of(2000, 1, 1);
            Student example = new Student(VALID_NAME_EXAMPLE, VALID_EMAIL_EXAMPLE, dob);
            int years = Period.between(dob, LocalDate.now()).getYears();
            assertEquals(years, example.getAge());
        }
    }

//    Contact Creation

    @Test
    @DisplayName("Test Student Creation")
    public void shouldReturnAllStudents(){
        studentService.addNewStudent("Student A", "a@gmail.com", LocalDate.now());
        studentService.addNewStudent("Student B", "b@gmail.com", LocalDate.now());
        assertEquals(2,studentService.getAllStudents().size());
    }

    @DisplayName("Repeat Student Creation for 5 times")
    @RepeatedTest(value = 5,
            name = "Repeating Student Creation Test {currentRepetition} of {totalRepetition}")
    public void shouldCrateStudentRepeatedly(){
        studentService.addNewStudent(
                VALID_NAME_EXAMPLE,
                VALID_EMAIL_EXAMPLE,
                LocalDate.now()
        );
        assertFalse(studentRepository.findAll().isEmpty());
        assertEquals(1,studentService.getAllStudents().size());
    }

//    Contact Deleting

    @Test
    @DisplayName("Should Delete Student By Id")
    public void shouldDeleteStudentById(){
        Student student = new Student(VALID_NAME_EXAMPLE,VALID_EMAIL_EXAMPLE, LocalDate.now());
        studentService.registerNewStudent(student);
        studentService.deleteStudentById(student.getId());
        assertTrue(studentService.getAllStudents().isEmpty());
    }

    @Test
    @DisplayName("Should Delete Student By Email")
    public void shouldDeleteStudentByEmail(){
        String email = VALID_EMAIL_EXAMPLE;
        studentService.addNewStudent(VALID_NAME_EXAMPLE, email, LocalDate.now());

        studentService.deleteStudentByEmail(email);
        assertTrue(studentService.getAllStudents().isEmpty());
    }

//    ParameterizedTests

    @DisplayName("Test Different Emails")
    @ParameterizedTest
    @ValueSource(strings = {"example@gmail.com", "example@yahoo.com","example@mail.ru"})
    public void shouldTestStudentCreationWithValueSources(String email){
        studentService.addNewStudent("exampleName", email, LocalDate.now());
        assertFalse(studentRepository.findAll().isEmpty());
        assertEquals(1,studentService.getAllStudents().size());
    }


//    Update Tests
    @Nested
    public class UpdateTests {

        private String initialEmail;
        private String updatedEmail;
        private String initialName;
        private String updatedName;

        @BeforeEach
        void setUp() {
            initialEmail = VALID_EMAIL_EXAMPLE;
            updatedEmail = "updated@gmail.com";
            initialName = VALID_NAME_EXAMPLE;
            updatedName = "updatedName";
            // Registering a student to update later
            studentService.addNewStudent(initialName,initialEmail, LocalDate.now());
        }

        @Test
        @DisplayName("Should Update Student Email")
        public void shouldUpdateStudentEmail() {
            studentService.updateStudentEmail(initialEmail, updatedEmail);
            assertFalse(studentRepository.findStudentByEmail(initialEmail).isPresent());
            assertTrue(studentRepository.findStudentByEmail(updatedEmail).isPresent());
        }

        @Test
        @DisplayName("Should Update Student Name")
        public void shouldUpdateStudentName() {
            assertFalse(studentRepository.findAll().isEmpty()); //verify the list is not empty
            assertEquals(1, studentRepository.findAll().size());

            studentService.updateStudentName(VALID_EMAIL_EXAMPLE, updatedName);
            Student updatedStudent = studentRepository.findStudentByEmail(VALID_EMAIL_EXAMPLE).orElse(null);

    //        Verify that the name has been updated
            assertNotNull(updatedStudent);
            assertNotEquals(initialName, updatedStudent.getName());
            assertEquals(updatedName, updatedStudent.getName());
        }
    }


}
