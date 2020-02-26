package io.bhpw3j.utils;

import io.bhpw3j.constants.BHPConstants;

import java.math.BigDecimal;

public class TransactionUtils {

    public static BigDecimal calcNecessaryNetworkFee(int transactionSize) {
        if (transactionSize > BHPConstants.MAX_FREE_TRANSACTION_SIZE) {
            int chargeableSize = transactionSize - BHPConstants.MAX_FREE_TRANSACTION_SIZE;
            return BHPConstants.FEE_PER_EXTRA_BYTE
                    .multiply(new BigDecimal(chargeableSize))
                    .add(BHPConstants.PRIORITY_THRESHOLD_FEE);
        } else {
            return BigDecimal.ZERO;
        }
    }

}
