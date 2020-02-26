package io.bhpw3j.protocol.exceptions;

import io.bhpw3j.protocol.core.Response.Error;

public class ErrorResponseException extends Exception {

    private Error error;

    public ErrorResponseException(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
