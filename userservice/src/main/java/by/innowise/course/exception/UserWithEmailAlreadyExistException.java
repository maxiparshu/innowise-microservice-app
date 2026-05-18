package by.innowise.course.exception;

public class UserWithEmailAlreadyExistException extends RuntimeException {

    public UserWithEmailAlreadyExistException(String email) {
        super("User with email: " + email + " already exist");
    }
}
