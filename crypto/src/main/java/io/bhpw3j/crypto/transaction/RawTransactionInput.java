package io.bhpw3j.crypto.transaction;

import io.bhpw3j.io.BinaryReader;
import io.bhpw3j.io.BinaryWriter;
import io.bhpw3j.io.BhpSerializable;
import io.bhpw3j.utils.ArrayUtils;
import io.bhpw3j.utils.BigIntegers;
import io.bhpw3j.utils.Numeric;

import java.io.IOException;
import java.util.Objects;

public class RawTransactionInput extends BhpSerializable {

    public String prevHash;

    public int prevIndex;

    public RawTransactionInput() {
    }

    public RawTransactionInput(String prevHash, int prevIndex) {
        this.prevHash = prevHash;
        this.prevIndex = prevIndex;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public int getPrevIndex() {
        return prevIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RawTransactionInput)) return false;
        RawTransactionInput that = (RawTransactionInput) o;
        return getPrevIndex() == that.getPrevIndex() &&
                Objects.equals(getPrevHash(), that.getPrevHash());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPrevHash(), getPrevIndex());
    }

    @Override
    public String toString() {
        return "TransactionInput{" +
                "prevHash='" + prevHash + '\'' +
                ", prevIndex=" + prevIndex +
                '}';
    }

    @Override
    public void deserialize(BinaryReader reader) throws IOException {
        this.prevHash = Numeric.toHexStringNoPrefix(ArrayUtils.reverseArray(reader.readBytes(32)));
        byte[] readBytes = reader.readBytes(2);
        this.prevIndex = Numeric.toBigInt(ArrayUtils.reverseArray(readBytes)).intValue();
    }

    @Override
    public void serialize(BinaryWriter writer) throws IOException {
        writer.write(ArrayUtils.reverseArray(Numeric.hexStringToByteArray(this.prevHash)));
        writer.write(BigIntegers.toLittleEndianByteArrayZeroPadded(this.prevIndex, 2));
    }
}
