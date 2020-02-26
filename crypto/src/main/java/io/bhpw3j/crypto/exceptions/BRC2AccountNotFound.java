package io.bhpw3j.crypto.exceptions;

/**
 * BRC2 specific account not found in the wallet.
 */
public class BRC2AccountNotFound extends Exception {

    public BRC2AccountNotFound() {
    }

    public BRC2AccountNotFound(String message) {
        super(message);
    }

    public BRC2AccountNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
