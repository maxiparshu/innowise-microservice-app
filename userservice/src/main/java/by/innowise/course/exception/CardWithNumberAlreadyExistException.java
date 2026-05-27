package by.innowise.course.exception;

public class CardWithNumberAlreadyExistException extends RuntimeException {
    public CardWithNumberAlreadyExistException(String number) {
        super("Card with number: " + number + " already exist");
    }
}
