package io.bhpw3j.contract.abi.exceptions;

/**
 * BRC3 parsing exception or invalid format.
 */
public class BRC3ParsingException extends BRC3Exception {

    public BRC3ParsingException(String message) {
        super(message);
    }

    public BRC3ParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
