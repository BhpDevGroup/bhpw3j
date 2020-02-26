package io.bhpw3j.protocol.core.methods.response;

import io.bhpw3j.protocol.core.Response;

public class BhpGetRawTransaction extends Response<String> {

    public String getRawTransaction() {
        return getResult();
    }

}
