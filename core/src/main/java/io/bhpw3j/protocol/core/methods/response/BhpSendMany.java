package io.bhpw3j.protocol.core.methods.response;

import io.bhpw3j.protocol.core.Response;

public class BhpSendMany extends Response<Transaction> {

    public Transaction getSendMany() {
        return getResult();
    }

}