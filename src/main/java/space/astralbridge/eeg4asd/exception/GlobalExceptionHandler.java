package space.astralbridge.eeg4asd.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.extern.slf4j.Slf4j;
import space.astralbridge.eeg4asd.dto.response.BasicResponseStandard;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)

    public ResponseEntity<BasicResponseStandard<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMsg = error.getDefaultMessage();
            errors.put(fieldName, errorMsg);
        });

        log.info("Validation Error: {}", errors);

        return new ResponseEntity<>(new BasicResponseStandard<>(429, "Validation Error", errors),
                HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BasicResponseStandard<Void> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        log.info("Method Not Allowed: {}", e.getMessage());

        return new BasicResponseStandard<>(405, "Method Not Allowed", null);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public BasicResponseStandard<Void> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.info("Not Found: {}", e.getMessage());

        return new BasicResponseStandard<>(404, "Not Found", null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public BasicResponseStandard<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        log.info("Bad Request: {}", e.getMessage());

        return new BasicResponseStandard<>(400, "Bad Request: " + e.getMessage(), null);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public BasicResponseStandard<String> handleNoSuchElementException(NoSuchElementException e) {
        log.info("Not Found: {}", e.getMessage());

        return new BasicResponseStandard<>(404, "Not Found", e.getMessage());
    }

    // @ExceptionHandler(HttpMediaTypeException.class)
    // public BasicResponseStandard<Void>
    // handleHttpMediaTypeNotSupportedException(HttpMediaTypeException e) {
    // log.info("Media Type Exception: {}", e.getMessage());

    // return new BasicResponseStandard<>(415, "Unsupported Media Type", null);
    // }

    @ExceptionHandler(Exception.class)
    public BasicResponseStandard<Void> handleException(Exception e) {
        e.printStackTrace();
        log.error("Internal Server Error: [{}]{}", e.getClass(), e.getMessage());

        return new BasicResponseStandard<>(500, "Internal Server Error", null);
    }

}
