package io.bhpw3j.protocol.core.methods.response;

import io.bhpw3j.protocol.core.Response;

public class BhpBlockHash extends Response<String> {

    public String getBlockHash() {
        return getResult();
    }

}