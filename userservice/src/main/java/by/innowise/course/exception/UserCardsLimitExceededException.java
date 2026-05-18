package by.innowise.course.exception;

public class UserCardsLimitExceededException extends RuntimeException {

    public UserCardsLimitExceededException() {
        super("User cannot have more than 5 payment cards");
    }
}
