package io.bhpw3j.crypto.exceptions;

/**
 * BRC2 format exception.
 */
public class BRC2InvalidFormat extends Exception {

    public BRC2InvalidFormat() {
    }

    public BRC2InvalidFormat(String message) {
        super(message);
    }

    public BRC2InvalidFormat(String message, Throwable cause) {
        super(message, cause);
    }
}
