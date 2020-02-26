package io.bhpw3j.crypto.exceptions;

/**
 * BRC2 format exception.
 */
public class BRC2InvalidPassphrase extends Exception {

    public BRC2InvalidPassphrase() {
    }

    public BRC2InvalidPassphrase(String message) {
        super(message);
    }

    public BRC2InvalidPassphrase(String message, Throwable cause) {
        super(message, cause);
    }
}
