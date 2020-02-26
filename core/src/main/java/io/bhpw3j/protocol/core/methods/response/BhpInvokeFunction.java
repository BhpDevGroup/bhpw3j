package io.bhpw3j.protocol.core.methods.response;

import io.bhpw3j.protocol.core.Response;

public class BhpInvokeFunction extends Response<InvocationResult> {

    public InvocationResult getInvocationResult() {
        return getResult();
    }

}
