package io.bhpw3j.protocol.core.methods.response;

import io.bhpw3j.protocol.core.Response;

public class BhpDumpPrivKey extends Response<String> {

    public String getDumpPrivKey() {
        return getResult();
    }

}