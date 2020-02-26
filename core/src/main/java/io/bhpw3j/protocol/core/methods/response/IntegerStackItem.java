package io.bhpw3j.protocol.core.methods.response;

import io.bhpw3j.model.types.StackItemType;

import java.math.BigInteger;

public class IntegerStackItem extends StackItem {

    public IntegerStackItem(BigInteger value) {
        super(StackItemType.INTEGER, value);
    }

    @Override
    public BigInteger getValue() {
        return (BigInteger) this.value;
    }
}
