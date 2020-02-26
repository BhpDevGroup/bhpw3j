package io.bhpw3j.protocol.core.methods.response;

import io.bhpw3j.protocol.core.Response;

public class BhpGetBlock extends Response<BhpBlock> {

    public BhpBlock getBlock() {
        return getResult();
    }

}
