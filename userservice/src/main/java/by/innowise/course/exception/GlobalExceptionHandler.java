package by.innowise.course.exception;

import by.innowise.course.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleUserNotFound(
            UserNotFoundException ex
    ) {

        return ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(PaymentCardNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleCardNotFound(
            PaymentCardNotFoundException ex
    ) {

        return ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(UserWithEmailAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDto handleUserWithEmailAlreadyExist(
            UserWithEmailAlreadyExistException ex
    ) {

        return ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(UserCardsLimitExceededException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDto handleUserCardsLimitExceeded(
            UserCardsLimitExceededException ex
    ) {

        return ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleValidationException(
            MethodArgumentNotValidException exception
    ) {

        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error ->
                        error.getField() + ": " + error.getDefaultMessage()
                )
                .orElse("Validation error");

        return ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(message)
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto handleAll(Exception ex) {
        return ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(ex.getMessage())
                .build();
    }
}
