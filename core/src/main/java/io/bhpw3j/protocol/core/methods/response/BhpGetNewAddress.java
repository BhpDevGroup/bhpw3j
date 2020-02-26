package io.bhpw3j.protocol.core.methods.response;

import io.bhpw3j.protocol.core.Response;

public class BhpGetNewAddress extends Response<String> {

    public String getAddress() {
        return getResult();
    }

}
