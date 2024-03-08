package com.iris.ares.reactGenerator.security.exception;


/**
 * Exception thrown to indicate that a license is invalid.
 */
public class LicenceInvalideException extends Exception {


    /**
     * Constructs a new LicenseInvalideException with no detail message.
     */
    public LicenceInvalideException() {
        super();
    }


    /**
     * Constructs a new LicenseInvalideException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     */
    public LicenceInvalideException(String message) {
        super(message);
    }


    /**
     * Constructs a new LicenseInvalideException with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     * @param cause   the cause (which is saved for later retrieval by the getCause() method).
     */
    public LicenceInvalideException(String message, Throwable cause) {
        super(message, cause);
    }


    /**
     * Constructs a new LicenseInvalideException with the specified cause and a detail message of
     * (cause==null ? null : cause.toString()) (which typically contains the class and detail message of cause).
     *
     * @param cause the cause (which is saved for later retrieval by the getCause() method).
     */
    public LicenceInvalideException(Throwable cause) {
        super(cause);
    }
}
