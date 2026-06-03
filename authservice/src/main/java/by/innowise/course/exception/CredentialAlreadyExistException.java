package by.innowise.course.exception;

public class CredentialAlreadyExistException extends IllegalArgumentException {
    public CredentialAlreadyExistException() {
        super("Credentials already exist for this user");
    }
}
