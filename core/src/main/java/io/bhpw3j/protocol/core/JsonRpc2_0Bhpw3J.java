package io.bhpw3j.protocol.core;

import io.bhpw3j.contract.ContractParameter;
import io.bhpw3j.protocol.Bhpw3j;
import io.bhpw3j.protocol.Bhpw3jService;
import io.bhpw3j.protocol.core.methods.response.BhpBlockCount;
import io.bhpw3j.protocol.core.methods.response.BhpBlockHash;
import io.bhpw3j.protocol.core.methods.response.BhpConnectionCount;
import io.bhpw3j.protocol.core.methods.response.BhpDumpPrivKey;
import io.bhpw3j.protocol.core.methods.response.BhpGetAccountState;
import io.bhpw3j.protocol.core.methods.response.BhpGetApplicationLog;
import io.bhpw3j.protocol.core.methods.response.BhpGetAssetState;
import io.bhpw3j.protocol.core.methods.response.BhpGetBalance;
import io.bhpw3j.protocol.core.methods.response.BhpGetBlock;
import io.bhpw3j.protocol.core.methods.response.BhpGetBlockSysFee;
import io.bhpw3j.protocol.core.methods.response.BhpGetClaimable;
import io.bhpw3j.protocol.core.methods.response.BhpGetContractState;
import io.bhpw3j.protocol.core.methods.response.BhpGetBrc5Balances;
import io.bhpw3j.protocol.core.methods.response.BhpGetNewAddress;
import io.bhpw3j.protocol.core.methods.response.BhpGetPeers;
import io.bhpw3j.protocol.core.methods.response.BhpGetRawBlock;
import io.bhpw3j.protocol.core.methods.response.BhpGetRawMemPool;
import io.bhpw3j.protocol.core.methods.response.BhpGetRawTransaction;
import io.bhpw3j.protocol.core.methods.response.BhpGetStorage;
import io.bhpw3j.protocol.core.methods.response.BhpGetTransaction;
import io.bhpw3j.protocol.core.methods.response.BhpGetTxOut;
import io.bhpw3j.protocol.core.methods.response.BhpGetUnspents;
import io.bhpw3j.protocol.core.methods.response.BhpGetValidators;
import io.bhpw3j.protocol.core.methods.response.BhpGetVersion;
import io.bhpw3j.protocol.core.methods.response.BhpGetWalletHeight;
import io.bhpw3j.protocol.core.methods.response.BhpInvoke;
import io.bhpw3j.protocol.core.methods.response.BhpInvokeFunction;
import io.bhpw3j.protocol.core.methods.response.BhpInvokeScript;
import io.bhpw3j.protocol.core.methods.response.BhpListAddress;
import io.bhpw3j.protocol.core.methods.response.BhpListPlugins;
import io.bhpw3j.protocol.core.methods.response.BhpSendMany;
import io.bhpw3j.protocol.core.methods.response.BhpSendRawTransaction;
import io.bhpw3j.protocol.core.methods.response.BhpSendToAddress;
import io.bhpw3j.protocol.core.methods.response.BhpSubmitBlock;
import io.bhpw3j.protocol.core.methods.response.BhpValidateAddress;
import io.bhpw3j.protocol.core.methods.response.TransactionOutput;
import io.bhpw3j.protocol.rx.JsonRpc2_0Rx;
import io.bhpw3j.utils.Async;
import rx.Observable;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

import static io.bhpw3j.utils.Numeric.cleanHexPrefix;
import static io.bhpw3j.utils.Strings.isEmpty;

/**
 * JSON-RPC 2.0 factory implementation.
 */
public class JsonRpc2_0Bhpw3J implements Bhpw3j {

    public static final int DEFAULT_BLOCK_TIME = 15 * 1000;

    protected final Bhpw3jService bhpw3JService;
    private final JsonRpc2_0Rx bhpw3jRx;
    private final long blockTime;
    private final ScheduledExecutorService scheduledExecutorService;

    public JsonRpc2_0Bhpw3J(Bhpw3jService bhpw3JService) {
        this(bhpw3JService, DEFAULT_BLOCK_TIME, Async.defaultExecutorService());
    }

    public JsonRpc2_0Bhpw3J(
            Bhpw3jService bhpw3JService, long pollingInterval,
            ScheduledExecutorService scheduledExecutorService) {
        this.bhpw3JService = bhpw3JService;
        this.bhpw3jRx = new JsonRpc2_0Rx(this, scheduledExecutorService);
        this.blockTime = pollingInterval;
        this.scheduledExecutorService = scheduledExecutorService;
    }

    @Override
    public Request<?, BhpGetVersion> getVersion() {
        return new Request<>(
                "getversion",
                Collections.<String>emptyList(),
                bhpw3JService,
                BhpGetVersion.class);
    }


    @Override
    public Request<?, BhpGetBlock> getBlock(String address, boolean returnFullTransactionObjects) {
        if (returnFullTransactionObjects) {
            return new Request<>(
                    "getblock",
                    Arrays.asList(address, 1),
                    bhpw3JService,
                    BhpGetBlock.class);
        } else {
            return getBlockHeader(address);
        }
    }

    @Override
    public Request<?, BhpGetRawBlock> getRawBlock(String address) {
        return new Request<>(
                "getblock",
                Arrays.asList(address, 0),
                bhpw3JService,
                BhpGetRawBlock.class);
    }

    @Override
    public Request<?, BhpGetBlock> getBlock(BlockParameterIndex blockIndex, boolean returnFullTransactionObjects) {
        if (returnFullTransactionObjects) {
            return new Request<>(
                    "getblock",
                    Arrays.asList(blockIndex.getBlockIndex(), 1),
                    bhpw3JService,
                    BhpGetBlock.class);
        } else {
            return getBlockHeader(blockIndex);
        }
    }

    @Override
    public Request<?, BhpGetRawBlock> getRawBlock(BlockParameterIndex blockIndex) {
        return new Request<>(
                "getblock",
                Arrays.asList(blockIndex.getBlockIndex(), 0),
                bhpw3JService,
                BhpGetRawBlock.class);
    }

    @Override
    public Request<?, BhpBlockCount> getBlockCount() {
        return new Request<>(
                "getblockcount",
                Collections.<String>emptyList(),
                bhpw3JService,
                BhpBlockCount.class);
    }

    @Override
    public Request<?, BhpBlockHash> getBestBlockHash() {
        return new Request<>(
                "getbestblockhash",
                Collections.<String>emptyList(),
                bhpw3JService,
                BhpBlockHash.class);
    }

    @Override
    public Request<?, BhpBlockHash> getBlockHash(BlockParameterIndex blockIndex) {
        return new Request<>(
                "getblockhash",
                Arrays.asList(blockIndex.getBlockIndex()),
                bhpw3JService,
                BhpBlockHash.class);
    }

    @Override
    public Request<?, BhpGetBlock> getBlockHeader(String hash) {
        return new Request<>(
                "getblockheader",
                Arrays.asList(hash, 1),
                bhpw3JService,
                BhpGetBlock.class);
    }

    @Override
    public Request<?, BhpGetBlock> getBlockHeader(BlockParameterIndex blockIndex) {
        return new Request<>(
                "getblockheader",
                Arrays.asList(blockIndex.getBlockIndex(), 1),
                bhpw3JService,
                BhpGetBlock.class);
    }

    @Override
    public Request<?, BhpGetRawBlock> getRawBlockHeader(String hash) {
        return new Request<>(
                "getblockheader",
                Arrays.asList(hash, 0),
                bhpw3JService,
                BhpGetRawBlock.class);
    }

    @Override
    public Request<?, BhpGetRawBlock> getRawBlockHeader(BlockParameterIndex blockIndex) {
        return new Request<>(
                "getblockheader",
                Arrays.asList(blockIndex.getBlockIndex(), 0),
                bhpw3JService,
                BhpGetRawBlock.class);
    }

    @Override
    public Request<?, BhpConnectionCount> getConnectionCount() {
        return new Request<>(
                "getconnectioncount",
                Collections.<String>emptyList(),
                bhpw3JService,
                BhpConnectionCount.class);
    }

    @Override
    public Request<?, BhpListAddress> listAddress() {
        return new Request<>(
                "listaddress",
                Collections.<String>emptyList(),
                bhpw3JService,
                BhpListAddress.class);
    }

    @Override
    public Request<?, BhpGetPeers> getPeers() {
        return new Request<>(
                "getpeers",
                Collections.<String>emptyList(),
                bhpw3JService,
                BhpGetPeers.class);
    }

    @Override
    public Request<?, BhpGetRawMemPool> getRawMemPool() {
        return new Request<>(
                "getrawmempool",
                Collections.<String>emptyList(),
                bhpw3JService,
                BhpGetRawMemPool.class);
    }

    @Override
    public Request<?, BhpGetValidators> getValidators() {
        return new Request<>(
                "getvalidators",
                Collections.<String>emptyList(),
                bhpw3JService,
                BhpGetValidators.class);
    }

    @Override
    public Request<?, BhpValidateAddress> validateAddress(String address) {
        return new Request<>(
                "validateaddress",
                Arrays.asList(address),
                bhpw3JService,
                BhpValidateAddress.class);
    }

    @Override
    public Request<?, BhpGetAccountState> getAccountState(String address) {
        return new Request<>(
                "getaccountstate",
                Arrays.asList(address),
                bhpw3JService,
                BhpGetAccountState.class);
    }

    @Override
    public Request<?, BhpGetNewAddress> getNewAddress() {
        return new Request<>(
                "getnewaddress",
                Collections.<String>emptyList(),
                bhpw3JService,
                BhpGetNewAddress.class);
    }

    @Override
    public Request<?, BhpGetWalletHeight> getWalletHeight() {
        return new Request<>(
                "getwalletheight",
                Collections.<String>emptyList(),
                bhpw3JService,
                BhpGetWalletHeight.class);
    }

    @Override
    public Request<?, BhpGetBlockSysFee> getBlockSysFee(BlockParameterIndex blockIndex) {
        return new Request<>(
                "getblocksysfee",
                Arrays.asList(blockIndex.getBlockIndex()),
                bhpw3JService,
                BhpGetBlockSysFee.class);
    }

    @Override
    public Request<?, BhpGetTxOut> getTxOut(String transactionHash, int txIndex) {
        return new Request<>(
                "gettxout",
                Arrays.asList(transactionHash, txIndex),
                bhpw3JService,
                BhpGetTxOut.class);
    }

    @Override
    public Request<?, BhpSendRawTransaction> sendRawTransaction(String rawTransactionHex) {
        return new Request<>(
                "sendrawtransaction",
                Arrays.asList(rawTransactionHex),
                bhpw3JService,
                BhpSendRawTransaction.class);
    }

    @Override
    public Request<?, BhpSendToAddress> sendToAddress(String assetId, String toAddress, String value) {
        return sendToAddress(assetId, toAddress, value, null, null);
    }

    @Override
    public Request<?, BhpSendToAddress> sendToAddress(String assetId, String toAddress, String value, String fee) {
        return sendToAddress(assetId, toAddress, value, fee, null);
    }

    @Override
    public Request<?, BhpSendToAddress> sendToAddress(String assetId, String toAddress, String value, String fee, String changeAddress) {
        return new Request<>(
                "sendtoaddress",
                Arrays.asList(assetId, toAddress, value, fee, changeAddress).stream()
                        .filter((param) -> (param != null && !isEmpty(param)))
                        .collect(Collectors.toList()),
                bhpw3JService,
                BhpSendToAddress.class);
    }

    @Override
    public Request<?, BhpGetTransaction> getTransaction(String txId) {
        return new Request<>(
                "getrawtransaction",
                Arrays.asList(txId, 1),
                bhpw3JService,
                BhpGetTransaction.class);
    }

    @Override
    public Request<?, BhpGetRawTransaction> getRawTransaction(String txId) {
        return new Request<>(
                "getrawtransaction",
                Arrays.asList(txId, 0),
                bhpw3JService,
                BhpGetRawTransaction.class);
    }

    @Override
    public Request<?, BhpGetBalance> getBalance(String assetId) {
        return new Request<>(
                "getbalance",
                Arrays.asList(cleanHexPrefix(assetId)),
                bhpw3JService,
                BhpGetBalance.class);
    }

    @Override
    public Request<?, BhpGetAssetState> getAssetState(String assetId) {
        return new Request<>(
                "getassetstate",
                Arrays.asList(cleanHexPrefix(assetId)),
                bhpw3JService,
                BhpGetAssetState.class);
    }

    @Override
    public Request<?, BhpSendMany> sendMany(List<TransactionOutput> outputs) {
        return sendMany(outputs, null, null);
    }

    @Override
    public Request<?, BhpSendMany> sendMany(List<TransactionOutput> outputs, String fee) {
        return sendMany(outputs, fee, null);
    }

    @Override
    public Request<?, BhpSendMany> sendMany(List<TransactionOutput> outputs, String fee, String changeAddress) {
        return new Request<>(
                "sendmany",
                Arrays.asList(outputs, fee, changeAddress).stream()
                        .filter((param) -> (param != null))
                        .collect(Collectors.toList()),
                bhpw3JService,
                BhpSendMany.class);
    }

    @Override
    public Request<?, BhpDumpPrivKey> dumpPrivKey(String address) {
        return new Request<>(
                "dumpprivkey",
                Arrays.asList(address),
                bhpw3JService,
                BhpDumpPrivKey.class);
    }

    @Override
    public Request<?, BhpGetStorage> getStorage(String contractAddress, HexParameter keyToLookUp) {
        return getStorage(contractAddress, keyToLookUp.getHexValue());
    }

    @Override
    public Request<?, BhpGetStorage> getStorage(String contractAddress, String keyToLookUpAsHexString) {
        return new Request<>(
                "getstorage",
                Arrays.asList(contractAddress, keyToLookUpAsHexString),
                bhpw3JService,
                BhpGetStorage.class);
    }

    @Override
    public Request<?, BhpInvoke> invoke(String contractScriptHash, List<ContractParameter> params) {
        return new Request<>(
                "invoke",
                Arrays.asList(contractScriptHash, params),
                bhpw3JService,
                BhpInvoke.class);
    }

    @Override
    public Request<?, BhpInvokeFunction> invokeFunction(String contractScriptHash, String functionName) {
        return invokeFunction(contractScriptHash, functionName, null);
    }

    @Override
    public Request<?, BhpInvokeFunction> invokeFunction(String contractScriptHash, String functionName, List<ContractParameter> params) {
        return new Request<>(
                "invokefunction",
                Arrays.asList(contractScriptHash, functionName, params).stream()
                        .filter((param) -> (param != null))
                        .collect(Collectors.toList()),
                bhpw3JService,
                BhpInvokeFunction.class);
    }

    @Override
    public Request<?, BhpInvokeScript> invokeScript(String script) {
        return new Request<>(
                "invokescript",
                Arrays.asList(script),
                bhpw3JService,
                BhpInvokeScript.class);
    }

    @Override
    public Request<?, BhpGetContractState> getContractState(String scriptHash) {
        return new Request<>(
                "getcontractstate",
                Arrays.asList(scriptHash),
                bhpw3JService,
                BhpGetContractState.class);
    }

    @Override
    public Request<?, BhpSubmitBlock> submitBlock(String serializedBlockAsHex) {
        return new Request<>(
                "submitblock",
                Arrays.asList(serializedBlockAsHex),
                bhpw3JService,
                BhpSubmitBlock.class);
    }

    @Override
    public Request<?, BhpGetUnspents> getUnspents(String address) {
        return new Request<>(
                "getutxoofaddress",
                Arrays.asList(address),
                bhpw3JService,
                BhpGetUnspents.class);
    }

    @Override
    public Request<?, BhpGetBrc5Balances> getBrc5Balances(String address) {
        return new Request<>(
                "getbrc5balances",
                Arrays.asList(address),
                bhpw3JService,
                BhpGetBrc5Balances.class);
    }

    @Override
    public Request<?, BhpGetClaimable> getClaimable(String address) {
        return new Request<>(
                "getclaimable",
                Arrays.asList(address),
                bhpw3JService,
                BhpGetClaimable.class);
    }

    @Override
    public Request<?, BhpListPlugins> listPlugins() {
        return new Request<>(
                "listplugins",
                Collections.<String>emptyList(),
                bhpw3JService,
                BhpListPlugins.class);
    }

    @Override
    public Observable<BhpGetBlock> blockObservable(boolean fullTransactionObjects) {
        return bhpw3jRx.blockObservable(fullTransactionObjects, blockTime);
    }

    @Override
    public Observable<BhpGetBlock> replayBlocksObservable(
            BlockParameter startBlock, BlockParameter endBlock,
            boolean fullTransactionObjects) {
        return bhpw3jRx.replayBlocksObservable(startBlock, endBlock, fullTransactionObjects);
    }

    @Override
    public Observable<BhpGetBlock> replayBlocksObservable(
            BlockParameter startBlock, BlockParameter endBlock,
            boolean fullTransactionObjects, boolean ascending) {
        return bhpw3jRx.replayBlocksObservable(startBlock, endBlock,
                fullTransactionObjects, ascending);
    }

    @Override
    public Observable<BhpGetBlock> catchUpToLatestBlockObservable(
            BlockParameter startBlock, boolean fullTransactionObjects,
            Observable<BhpGetBlock> onCompleteObservable) {
        return bhpw3jRx.catchUpToLatestBlockObservable(
                startBlock, fullTransactionObjects, onCompleteObservable);
    }

    @Override
    public Observable<BhpGetBlock> catchUpToLatestBlockObservable(
            BlockParameter startBlock, boolean fullTransactionObjects) {
        return bhpw3jRx.catchUpToLatestBlockObservable(startBlock, fullTransactionObjects);
    }

    @Override
    public Observable<BhpGetBlock> catchUpToLatestAndSubscribeToNewBlocksObservable(
            BlockParameter startBlock, boolean fullTransactionObjects) {
        return bhpw3jRx.catchUpToLatestAndSubscribeToNewBlocksObservable(
                startBlock, fullTransactionObjects, blockTime);
    }

    @Override
    public Request<?, BhpGetApplicationLog> getApplicationLog(String txId) {
        return new Request<>(
                "getapplicationlog",
                Collections.singletonList(txId),
                bhpw3JService,
                BhpGetApplicationLog.class);
    }

    @Override
    public void shutdown() {
        scheduledExecutorService.shutdown();
        try {
            bhpw3JService.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to close bhpw3j service", e);
        }
    }
}
