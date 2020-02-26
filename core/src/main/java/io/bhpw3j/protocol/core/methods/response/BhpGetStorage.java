package io.bhpw3j.protocol.core.methods.response;

import io.bhpw3j.protocol.core.Response;

public class BhpGetStorage extends Response<String> {

    public String getStorage() {
        return getResult();
    }

}
