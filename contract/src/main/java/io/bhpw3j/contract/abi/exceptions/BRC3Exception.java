package io.bhpw3j.contract.abi.exceptions;

/**
 * Base class for BRC3 exceptions.
 */
public class BRC3Exception extends Exception {

    public BRC3Exception(String message) {
        super(message);
    }

    public BRC3Exception(String message, Throwable cause) {
        super(message, cause);
    }
}
