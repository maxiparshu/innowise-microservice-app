package by.innowise.course.exception;

public class PaymentCardNotFoundException extends RuntimeException {
    public PaymentCardNotFoundException(Long id) {
        super("Payment card found with id: " + id);
    }
}
