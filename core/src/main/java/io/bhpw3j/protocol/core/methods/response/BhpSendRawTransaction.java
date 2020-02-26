package io.bhpw3j.protocol.core.methods.response;

import io.bhpw3j.protocol.core.Response;

public class BhpSendRawTransaction extends Response<Boolean> {

    public Boolean getSendRawTransaction() {
        return getResult();
    }

}