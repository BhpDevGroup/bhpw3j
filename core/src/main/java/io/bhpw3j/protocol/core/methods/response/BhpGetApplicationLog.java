package io.bhpw3j.protocol.core.methods.response;

import io.bhpw3j.protocol.core.Response;

public class BhpGetApplicationLog extends Response<BhpApplicationLog> {

    public BhpApplicationLog getApplicationLog() {
        return getResult();
    }

}
