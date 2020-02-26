package io.bhpw3j.protocol.core.methods.response;

import io.bhpw3j.protocol.core.Response;

public class BhpSubmitBlock extends Response<Boolean> {

    public Boolean getSubmitBlock() {
        return getResult();
    }

}