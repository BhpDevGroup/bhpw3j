package io.bhpw3j.protocol.core.methods.response;

import io.bhpw3j.protocol.core.Response;

import java.util.List;

public class BhpGetRawMemPool extends Response<List<String>> {

    public List<String> getAddresses() {
        return getResult();
    }

}
