package io.bhpw3j.protocol.core;

import io.bhpw3j.contract.ContractParameter;
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

import java.util.List;

/**
 * Core BHP JSON-RPC API.
 */
public interface Bhp {

    // API 2.9.*

    Request<?, BhpGetVersion> getVersion();

    Request<?, BhpGetBlock> getBlock(String address, boolean returnFullTransactionObjects);

    Request<?, BhpGetRawBlock> getRawBlock(String address);

    Request<?, BhpGetBlock> getBlock(BlockParameterIndex blockIndex, boolean returnFullTransactionObjects);

    Request<?, BhpGetRawBlock> getRawBlock(BlockParameterIndex blockIndex);

    Request<?, BhpBlockCount> getBlockCount();

    Request<?, BhpBlockHash> getBestBlockHash();

    Request<?, BhpBlockHash> getBlockHash(BlockParameterIndex blockIndex);

    Request<?, BhpGetBlock> getBlockHeader(String hash);

    Request<?, BhpGetBlock> getBlockHeader(BlockParameterIndex blockIndex);

    Request<?, BhpGetRawBlock> getRawBlockHeader(String hash);

    Request<?, BhpGetRawBlock> getRawBlockHeader(BlockParameterIndex blockIndex);

    Request<?, BhpConnectionCount> getConnectionCount();

    Request<?, BhpListAddress> listAddress();

    Request<?, BhpGetPeers> getPeers();

    Request<?, BhpGetRawMemPool> getRawMemPool();

    Request<?, BhpGetValidators> getValidators();

    Request<?, BhpValidateAddress> validateAddress(String address);

    Request<?, BhpGetAccountState> getAccountState(String address);

    Request<?, BhpGetNewAddress> getNewAddress();

    Request<?, BhpGetWalletHeight> getWalletHeight();

    Request<?, BhpGetBlockSysFee> getBlockSysFee(BlockParameterIndex blockIndex);

    Request<?, BhpGetTxOut> getTxOut(String transactionHash, int txIndex);

    Request<?, BhpSendRawTransaction> sendRawTransaction(String rawTransactionHex);

    Request<?, BhpSendToAddress> sendToAddress(String assetId, String toAddress, String value);

    Request<?, BhpSendToAddress> sendToAddress(String assetId, String toAddress, String value, String fee);

    Request<?, BhpSendToAddress> sendToAddress(String assetId, String toAddress, String value, String fee, String changeAddress);

    Request<?, BhpGetTransaction> getTransaction(String txId);

    Request<?, BhpGetRawTransaction> getRawTransaction(String txId);

    Request<?, BhpGetBalance> getBalance(String assetId);

    Request<?, BhpGetAssetState> getAssetState(String assetId);

    Request<?, BhpSendMany> sendMany(List<TransactionOutput> outputs);

    Request<?, BhpSendMany> sendMany(List<TransactionOutput> outputs, String fee);

    Request<?, BhpSendMany> sendMany(List<TransactionOutput> outputs, String fee, String changeAddress);

    Request<?, BhpDumpPrivKey> dumpPrivKey(String address);

    Request<?, BhpGetStorage> getStorage(String contractAddress, HexParameter keyToLookUp);

    Request<?, BhpGetStorage> getStorage(String contractAddress, String keyToLookUpAsHexString);

    Request<?, BhpInvoke> invoke(String contractScriptHash, List<ContractParameter> params);

    Request<?, BhpInvokeFunction> invokeFunction(String contractScriptHash, String functionName);

    Request<?, BhpInvokeFunction> invokeFunction(String contractScriptHash, String functionName, List<ContractParameter> params);

    Request<?, BhpInvokeScript> invokeScript(String script);

    Request<?, BhpGetContractState> getContractState(String scriptHash);

    Request<?, BhpSubmitBlock> submitBlock(String serializedBlockAsHex);

    // API 2.10.*

    Request<?, BhpGetUnspents> getUnspents(String address);

    Request<?, BhpGetBrc5Balances> getBrc5Balances(String address);

    Request<?, BhpGetClaimable> getClaimable(String address);

    Request<?, BhpListPlugins> listPlugins();

    // Plugins

    Request<?, BhpGetApplicationLog> getApplicationLog(String txId);

}
