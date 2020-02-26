package io.bhpw3j.contract;

import io.bhpw3j.io.BinaryReader;
import io.bhpw3j.io.BinaryWriter;
import io.bhpw3j.io.BhpSerializable;
import io.bhpw3j.model.types.ContractParameterType;

import java.io.IOException;

public class ContractParameterTypeSerializable extends BhpSerializable {

    private ContractParameterType contractParameterType;

    public ContractParameterTypeSerializable() {
    }

    public ContractParameterTypeSerializable(ContractParameterType contractParameterType) {
        this.contractParameterType = contractParameterType;
    }

    public ContractParameterType getContractParameterType() {
        return contractParameterType;
    }

    @Override
    public void deserialize(BinaryReader reader) throws IOException {
        this.contractParameterType = ContractParameterType.valueOf(reader.readByte());
    }

    @Override
    public void serialize(BinaryWriter writer) throws IOException {
        writer.writeByte(contractParameterType.byteValue());
    }

}
