package io.bhpw3j.transaction;

import io.bhpw3j.crypto.transaction.RawTransaction;
import io.bhpw3j.io.BinaryReader;
import io.bhpw3j.io.BinaryWriter;
import io.bhpw3j.model.types.TransactionType;

public class ContractTransaction extends RawTransaction {

    public ContractTransaction() { }

    protected ContractTransaction(Builder builder) {
        super(builder);
    }

    @Override
    public void serializeExclusive(BinaryWriter writer) {
        // no type-specific serialization.
    }

    @Override
    public void deserializeExclusive(BinaryReader reader) {
        // no type-specific deserialization.
    }

    public static class Builder extends RawTransaction.Builder<Builder> {

        public Builder() {
            super();
            transactionType(TransactionType.CONTRACT_TRANSACTION);
        }

        @Override
        public ContractTransaction build() {
            return new ContractTransaction(this);
        }
    }
}
