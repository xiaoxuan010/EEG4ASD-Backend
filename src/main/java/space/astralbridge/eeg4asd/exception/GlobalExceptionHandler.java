package space.astralbridge.eeg4asd.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeException;
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
    public BasicResponseStandard<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMsg = error.getDefaultMessage();
            errors.put(fieldName, errorMsg);
        });

        return new BasicResponseStandard<>(HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BasicResponseStandard<Void> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        return new BasicResponseStandard<>(HttpStatus.METHOD_NOT_ALLOWED, null);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public BasicResponseStandard<Void> handleNoHandlerFoundException(NoHandlerFoundException e) {
        return new BasicResponseStandard<>(HttpStatus.NOT_FOUND, null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public BasicResponseStandard<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        return new BasicResponseStandard<>(400, "Bad Request: " + e.getMessage(), null);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public BasicResponseStandard<String> handleNoSuchElementException(NoSuchElementException e) {
        return new BasicResponseStandard<>(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeException.class)
    public BasicResponseStandard<Void> handleHttpMediaTypeNotSupportedException(HttpMediaTypeException e) {
        return new BasicResponseStandard<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE, null);
    }

    @ExceptionHandler(Exception.class)
    public BasicResponseStandard<Void> handleException(Exception e) {
        e.printStackTrace();
        log.error("Internal Server Error: [{}]{}", e.getClass(), e.getMessage());

        return new BasicResponseStandard<>(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

}
