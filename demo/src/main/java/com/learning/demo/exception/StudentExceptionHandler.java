package com.learning.demo.exception;

import com.learning.demo.student.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice //responsible to handle exceptions
public class StudentExceptionHandler {

    @ExceptionHandler(value = {StudentNotFoundException.class})
    public ResponseEntity<Object> handleStudentNotFoundException
            (StudentNotFoundException exception) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        StudentException studentException = new StudentException(
                exception.getMessage(),
                exception.getCause(),
                notFound
        );

        return new ResponseEntity<>(studentException, notFound);
    }

    @ExceptionHandler(value = {
            DuplicateEmailException.class,
            InvalidDataException.class,
            SameEmailUpdateException.class})
    public ResponseEntity<Object> handleNotAcceptableExceptions(Exception exception) {
        HttpStatus notAccepted = HttpStatus.NOT_ACCEPTABLE;
        StudentException studentException = new StudentException(
                exception.getMessage(),
                exception.getCause(),
                notAccepted
        );
        return new ResponseEntity<>(studentException, notAccepted);
    }

    @ExceptionHandler(value = {
            BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestExceptions(Exception exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        StudentException studentException = new StudentException(
                exception.getMessage(),
                exception.getCause(),
                badRequest
        );
        return new ResponseEntity<>(studentException, badRequest);
    }


//    @ExceptionHandler(value = {InvalidDataException.class})
//    public ResponseEntity<Object> handleInvalidDataException
//            (InvalidDataException exception) {
//        HttpStatus notAccepted = HttpStatus.NOT_ACCEPTABLE;
//        StudentException studentException = new StudentException(
//                exception.getMessage(),
//                exception.getCause(),
//                notAccepted
//        );
//
//        return new ResponseEntity<>(studentException, notAccepted);
//    }

//     Generic exception handler
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleGeneralException(Exception exception) {
        HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
        StudentException studentException = new StudentException(
                "An unexpected error occurred",
                exception.getCause(),
                internalServerError
        );

        return new ResponseEntity<>(studentException, internalServerError);
    }

}
