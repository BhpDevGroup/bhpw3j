package io.bhpw3j.protocol.core.methods.response;

import io.bhpw3j.protocol.core.Response;

public class BhpGetTransaction extends Response<Transaction> {

    public Transaction getTransaction() {
        return getResult();
    }

}
