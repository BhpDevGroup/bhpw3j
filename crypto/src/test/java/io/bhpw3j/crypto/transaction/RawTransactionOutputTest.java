package io.bhpw3j.crypto.transaction;

import io.bhpw3j.model.types.BhpAsset;
import org.junit.Test;

import static org.junit.Assert.*;

public class RawTransactionOutputTest {

    @Test
    public void testEqualsWithDifferentDecimalNotation() {
        RawTransactionOutput o1 = new RawTransactionOutput(BhpAsset.HASH_ID, "15983.0", "address");
        RawTransactionOutput o2 = new RawTransactionOutput(BhpAsset.HASH_ID, "15983", "address");
        assertEquals(o1, o2);
    }

}