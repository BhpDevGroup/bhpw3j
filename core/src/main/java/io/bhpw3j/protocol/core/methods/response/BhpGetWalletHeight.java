package io.bhpw3j.protocol.core.methods.response;

import io.bhpw3j.protocol.core.Response;

import java.math.BigInteger;

public class BhpGetWalletHeight extends Response<BigInteger> {

    public BigInteger getHeight() {
        return getResult();
    }

}
