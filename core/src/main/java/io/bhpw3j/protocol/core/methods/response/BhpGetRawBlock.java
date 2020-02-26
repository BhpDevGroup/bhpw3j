package io.bhpw3j.protocol.core.methods.response;

import io.bhpw3j.protocol.core.Response;

public class BhpGetRawBlock extends Response<String> {

    public String getRawBlock() {
        return getResult();
    }

}
