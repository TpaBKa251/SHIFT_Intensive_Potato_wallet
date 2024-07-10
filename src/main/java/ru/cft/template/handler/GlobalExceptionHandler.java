package ru.cft.template.handler;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import ru.cft.template.exception.AccessRightsException;
import ru.cft.template.exception.BadTransactionException;
import ru.cft.template.exception.SessionNotFoundException;
import ru.cft.template.exception.WalletNotFoundException;
import ru.cft.template.model.response.ExceptionResponse;

import java.sql.SQLException;
import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = SessionNotFoundException.class)
    public ResponseEntity<Object> handleSessionNotFoundException(SessionNotFoundException ex) {
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = AccessRightsException.class)
    public ResponseEntity<Object> handleAccessRightException(AccessRightsException ex) {
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = WalletNotFoundException.class)
    public ResponseEntity<Object> handleWalletNotFoundException(WalletNotFoundException ex) {
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ExceptionResponse> handlerMethodArgumentValidExceptions(MethodArgumentNotValidException exception, WebRequest request) {
        String errors = "";
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            errors += fieldError.getDefaultMessage() + ", ";
        }

        ExceptionResponse exceptionResponse = new ExceptionResponse(errors);

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = BadTransactionException.class)
    public ResponseEntity<Object> handleBadTransactionException(BadTransactionException ex) {
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateException(IllegalStateException ex) {
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UsernameNotFoundException ex) {
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        if (ex.getMessage().contains("users_birth_date_check")) {
            return new ResponseEntity<>("Age must be at least 18", HttpStatus.BAD_REQUEST);
        }
        else if (ex.getMessage().contains("duplicate key")) {
            return new ResponseEntity<>("User with this "
                    + ex.getMessage().substring(ex.getMessage().indexOf('(') + 1, ex.getMessage().indexOf(')'))
                    + " already exists", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        return new ResponseEntity<>("Constraint violation: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex) {
        return new ResponseEntity<>("Constraint violation: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

