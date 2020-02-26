package io.bhpw3j.protocol.core.methods.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BhpBlock {

    @JsonProperty("hash")
    private String hash;

    @JsonProperty("size")
    private long size;

    @JsonProperty("version")
    private int version;

    @JsonProperty("previousblockhash")
    private String prevBlockHash;

    @JsonProperty("merkleroot")
    private String merkleRootHash;

    @JsonProperty("time")
    private long time;

    @JsonProperty("index")
    private long index;

    @JsonProperty("nonce")
    private String nonce;

    @JsonProperty("nextconsensus")
    private String nextConsensus;

    @JsonProperty("script")
    private Script script;

    @JsonProperty("tx")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Transaction> transactions;

    @JsonProperty("confirmations")
    private int confirmations;

    @JsonProperty("nextblockhash")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nextBlockHash;

    public BhpBlock() {
    }

    public BhpBlock(String hash, long size, int version, String prevBlockHash, String merkleRootHash, long time, long index, String nonce, String nextConsensus, Script script, List<Transaction> transactions, int confirmations, String nextBlockHash) {
        this.hash = hash;
        this.size = size;
        this.version = version;
        this.prevBlockHash = prevBlockHash;
        this.merkleRootHash = merkleRootHash;
        this.time = time;
        this.index = index;
        this.nonce = nonce;
        this.nextConsensus = nextConsensus;
        this.script = script;
        this.transactions = transactions;
        this.confirmations = confirmations;
        this.nextBlockHash = nextBlockHash;
    }

    public String getHash() {
        return hash;
    }

    public long getSize() {
        return size;
    }

    public int getVersion() {
        return version;
    }

    public String getPrevBlockHash() {
        return prevBlockHash;
    }

    public String getMerkleRootHash() {
        return merkleRootHash;
    }

    public long getTime() {
        return time;
    }

    public long getIndex() {
        return index;
    }

    public String getNonce() {
        return nonce;
    }

    public String getNextConsensus() {
        return nextConsensus;
    }

    public Script getScript() {
        return script;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public int getConfirmations() {
        return confirmations;
    }

    public String getNextBlockHash() {
        return nextBlockHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BhpBlock)) return false;
        BhpBlock bhpBlock = (BhpBlock) o;
        return getSize() == bhpBlock.getSize() &&
                getVersion() == bhpBlock.getVersion() &&
                getTime() == bhpBlock.getTime() &&
                getIndex() == bhpBlock.getIndex() &&
                getConfirmations() == bhpBlock.getConfirmations() &&
                Objects.equals(getHash(), bhpBlock.getHash()) &&
                Objects.equals(getPrevBlockHash(), bhpBlock.getPrevBlockHash()) &&
                Objects.equals(getMerkleRootHash(), bhpBlock.getMerkleRootHash()) &&
                Objects.equals(getNonce(), bhpBlock.getNonce()) &&
                Objects.equals(getNextConsensus(), bhpBlock.getNextConsensus()) &&
                Objects.equals(getScript(), bhpBlock.getScript()) &&
                Objects.equals(getTransactions(), bhpBlock.getTransactions()) &&
                Objects.equals(getNextBlockHash(), bhpBlock.getNextBlockHash());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHash(), getSize(), getVersion(), getPrevBlockHash(), getMerkleRootHash(), getTime(), getIndex(), getNonce(), getNextConsensus(), getScript(), getTransactions(), getConfirmations(), getNextBlockHash());
    }

    @Override
    public String toString() {
        return "BhpBlock{" +
                "hash='" + hash + '\'' +
                ", size=" + size +
                ", version=" + version +
                ", prevBlockHash='" + prevBlockHash + '\'' +
                ", merkleRootHash='" + merkleRootHash + '\'' +
                ", time=" + time +
                ", index=" + index +
                ", nonce='" + nonce + '\'' +
                ", nextConsensus='" + nextConsensus + '\'' +
                ", script=" + script +
                ", transactions=" + transactions +
                ", confirmations=" + confirmations +
                ", nextBlockHash='" + nextBlockHash + '\'' +
                '}';
    }
}
