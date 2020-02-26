package io.bhpw3j.protocol.core.methods.response;

import io.bhpw3j.protocol.core.Response;

public class BhpGetTxOut extends Response<TransactionOutput> {

    public TransactionOutput getTransaction() {
        return getResult();
    }

}
