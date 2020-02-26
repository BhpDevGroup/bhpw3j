package io.bhpw3j.protocol.core.methods.response;

import io.bhpw3j.protocol.core.Response;

public class BhpConnectionCount extends Response<Integer> {

    public Integer getCount() {
        return getResult();
    }

}
