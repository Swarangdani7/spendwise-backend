package com.swarang.spendwise.exception;

import com.swarang.spendwise.dto.error.ErrorResponseDTO;
import com.swarang.spendwise.dto.error.InvalidJsonResponseDTO;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNameNotFoundException(UsernameNotFoundException ex){
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND,
                ex.getMessage());
        return new ResponseEntity<>(errorResponseDTO, errorResponseDTO.error());
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleTransactionNotFoundException(TransactionNotFoundException ex){
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND,
                ex.getMessage());
        return new ResponseEntity<>(errorResponseDTO, errorResponseDTO.error());
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponseDTO> handleJwtException(JwtException ex){
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED,
                "Invalid JWT token: " + ex.getMessage());
        return new ResponseEntity<>(errorResponseDTO, errorResponseDTO.error());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthenticationException(AuthenticationException ex){
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED,
                "Authentication failed: " + ex.getMessage());
        return new ResponseEntity<>(errorResponseDTO, errorResponseDTO.error());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleSignupException(EmailAlreadyExistsException ex){
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT,
                ex.getMessage());
        return new ResponseEntity<>(errorResponseDTO, errorResponseDTO.error());
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<ErrorResponseDTO> handleMissingCookieException(MissingRequestCookieException ex){
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST,
                "Session Terminated: " + ex.getMessage());
        return new ResponseEntity<>(errorResponseDTO, errorResponseDTO.error());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidJsonTypeException(HttpMessageNotReadableException ex){
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST,
                "Invalid Type: " + ex.getMessage());
        return new ResponseEntity<>(errorResponseDTO, errorResponseDTO.error());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex){
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST,
                "Invalid value for field: " + ex.getMessage());
        return new ResponseEntity<>(errorResponseDTO, errorResponseDTO.error());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<InvalidJsonResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        List<Map<String, String>> listFieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> Map.of(
                        "field", fieldError.getField(),
                        "message", fieldError.getDefaultMessage()
                ))
                .toList();

        InvalidJsonResponseDTO errorResponseDTO = new InvalidJsonResponseDTO(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST,
                "Invalid JSON fields",
                listFieldErrors);

        return new ResponseEntity<>(errorResponseDTO, errorResponseDTO.error());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleRuntimeException(Exception ex){
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred: ");
        return new ResponseEntity<>(errorResponseDTO, errorResponseDTO.error());
    }
}
