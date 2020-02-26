package io.bhpw3j.protocol.core.methods.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.bhpw3j.protocol.core.Response;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

public class BhpGetBrc5Balances extends Response<BhpGetBrc5Balances.Balances> {

    public Balances getBalances() {
        return getResult();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Balances {

        @JsonProperty("balance")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        private List<Brc5Balance> balances;

        @JsonProperty("address")
        private String address;

        public Balances() {
        }

        public Balances(List<Brc5Balance> balances, String address) {
            this.balances = balances;
            this.address = address;
        }

        public List<Brc5Balance> getBalances() {
            return balances;
        }

        public String getAddress() {
            return address;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Balances)) return false;
            Balances balances1 = (Balances) o;
            return Objects.equals(getBalances(), balances1.getBalances()) &&
                    Objects.equals(getAddress(), balances1.getAddress());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getBalances(), getAddress());
        }

        @Override
        public String toString() {
            return "Balances{" +
                    "balances=" + balances +
                    ", address='" + address + '\'' +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Brc5Balance {

        @JsonProperty("asset_hash")
        private String assetHash;

        @JsonProperty("amount")
        private String amount;

        @JsonProperty("last_updated_block")
        private BigInteger lastUpdatedBlock;

        public Brc5Balance() {
        }

        public Brc5Balance(String assetHash, String amount, BigInteger lastUpdatedBlock) {
            this.assetHash = assetHash;
            this.amount = amount;
            this.lastUpdatedBlock = lastUpdatedBlock;
        }

        public String getAssetHash() {
            return assetHash;
        }

        public String getAmount() {
            return amount;
        }

        public BigInteger getLastUpdatedBlock() {
            return lastUpdatedBlock;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Brc5Balance)) return false;
            Brc5Balance that = (Brc5Balance) o;
            return Objects.equals(assetHash, that.assetHash) &&
                    Objects.equals(amount, that.amount) &&
                    Objects.equals(lastUpdatedBlock, that.lastUpdatedBlock);
        }

        @Override
        public int hashCode() {
            return Objects.hash(assetHash, amount, lastUpdatedBlock);
        }

        @Override
        public String toString() {
            return "Brc5Balance{" +
                    "assetHash='" + assetHash + '\'' +
                    ", amount='" + amount + '\'' +
                    ", lastUpdatedBlock=" + lastUpdatedBlock +
                    '}';
        }
    }

}
