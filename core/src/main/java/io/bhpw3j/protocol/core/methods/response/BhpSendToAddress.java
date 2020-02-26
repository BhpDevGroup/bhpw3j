package io.bhpw3j.protocol.core.methods.response;

import io.bhpw3j.protocol.core.Response;

public class BhpSendToAddress extends Response<Transaction> {

    public Transaction getSendToAddress() {
        return getResult();
    }

}