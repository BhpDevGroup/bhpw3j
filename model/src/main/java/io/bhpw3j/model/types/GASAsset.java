package io.bhpw3j.model.types;

import java.math.BigDecimal;
import java.math.BigInteger;

public class GASAsset {

    public static final String NAME = "BHPGas";

    public static final String HASH_ID = "0xa60b5dbb2b50022e3179a5a129b4d90bbb5bf5caabc40893fcdb83703e751225";

    public static final AssetType TYPE = AssetType.UTILITY_TOKEN;

    public static BigInteger toBigInt(String value) {
        if (value == null) {
            return BigInteger.ZERO;
        }
        return new BigDecimal(value).multiply(BigDecimal.valueOf(100000000)).toBigInteger();
    }

}
