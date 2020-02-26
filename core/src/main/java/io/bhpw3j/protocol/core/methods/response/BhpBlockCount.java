package io.bhpw3j.protocol.core.methods.response;

import io.bhpw3j.protocol.core.Response;

import java.math.BigInteger;

public class BhpBlockCount extends Response<BigInteger> {

    public BigInteger getBlockIndex() {
        return getResult();
    }

}
