package cm.landry.atm_machine.exception;

/**
 * Exception thrown when there are insufficient funds for a transaction.
 */
public class InsufficientFundsException extends RuntimeException {

    /**
     * Constructs a new InsufficientFundsException with the specified detail message.
     *
     * @param message the detail message
     */
    public InsufficientFundsException(String message) {
        super(message);
    }

    /**
     * Constructs a new InsufficientFundsException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public InsufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new InsufficientFundsException with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public InsufficientFundsException(Throwable cause) {
        super(cause);
    }
}
