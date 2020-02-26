package io.bhpw3j.protocol.core.methods.response;

import io.bhpw3j.protocol.core.Response;

public class BhpGetBlockSysFee extends Response<String> {

    public String getFee() {
        return getResult();
    }

}
