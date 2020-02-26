package io.bhpw3j.protocol;

import io.bhpw3j.model.types.AssetType;
import io.bhpw3j.model.types.GASAsset;
import io.bhpw3j.model.types.BhpAsset;
import io.bhpw3j.model.types.TransactionAttributeUsageType;
import io.bhpw3j.model.types.TransactionType;
import io.bhpw3j.protocol.core.BlockParameterIndex;
import io.bhpw3j.protocol.core.methods.response.BhpBlock;
import io.bhpw3j.protocol.core.methods.response.BhpBlockCount;
import io.bhpw3j.protocol.core.methods.response.BhpBlockHash;
import io.bhpw3j.protocol.core.methods.response.BhpConnectionCount;
import io.bhpw3j.protocol.core.methods.response.BhpDumpPrivKey;
import io.bhpw3j.protocol.core.methods.response.BhpGetAccountState;
import io.bhpw3j.protocol.core.methods.response.BhpGetAssetState;
import io.bhpw3j.protocol.core.methods.response.BhpGetBalance;
import io.bhpw3j.protocol.core.methods.response.BhpGetBlock;
import io.bhpw3j.protocol.core.methods.response.BhpGetBlockSysFee;
import io.bhpw3j.protocol.core.methods.response.BhpGetPeers;
import io.bhpw3j.protocol.core.methods.response.BhpGetRawBlock;
import io.bhpw3j.protocol.core.methods.response.BhpGetRawMemPool;
import io.bhpw3j.protocol.core.methods.response.BhpGetRawTransaction;
import io.bhpw3j.protocol.core.methods.response.BhpGetTransaction;
import io.bhpw3j.protocol.core.methods.response.BhpGetTxOut;
import io.bhpw3j.protocol.core.methods.response.BhpGetUnspents;
import io.bhpw3j.protocol.core.methods.response.BhpGetUnspents.Balance;
import io.bhpw3j.protocol.core.methods.response.BhpGetUnspents.Unspents;
import io.bhpw3j.protocol.core.methods.response.BhpGetValidators;
import io.bhpw3j.protocol.core.methods.response.BhpGetVersion;
import io.bhpw3j.protocol.core.methods.response.BhpGetWalletHeight;
import io.bhpw3j.protocol.core.methods.response.BhpListAddress;
import io.bhpw3j.protocol.core.methods.response.BhpListAddress.Address;
import io.bhpw3j.protocol.core.methods.response.BhpValidateAddress;
import io.bhpw3j.protocol.core.methods.response.Script;
import io.bhpw3j.protocol.core.methods.response.TransactionAttribute;
import io.bhpw3j.protocol.core.methods.response.TransactionInput;
import io.bhpw3j.protocol.core.methods.response.TransactionOutput;
import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsNull;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static io.bhpw3j.utils.Numeric.prependHexPrefix;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

// This test class uses a static container which is reused in every test to avoid the long startup
// time of the container. Therefore only tests that perform read-only operations should be added
// here.
public class Bhpw3JReadOnlyIntegrationTest extends Bhpw3JIntegrationTest {

    @ClassRule
    public static GenericContainer privateNetContainer = new GenericContainer(PRIVNET_CONTAINER)
            .withExposedPorts(EXPOSED_INTERNAL_PORT_BHP_DOTNET)
            .waitingFor(Wait.forListeningPort());

    @Override
    protected GenericContainer getPrivateNetContainer() {
        return privateNetContainer;
    }

    @Test
    public void testGetVersion() throws IOException {
        BhpGetVersion version = getBhpw3j().getVersion().send();
        BhpGetVersion.Result versionResult = version.getVersion();
        assertNotNull(versionResult);
        assertThat(versionResult.getUserAgent(), not(isEmptyString()));
        assertThat(versionResult.getNonce(), is(greaterThanOrEqualTo(0L)));
        assertThat(versionResult.getPort(), is(greaterThanOrEqualTo(0)));
    }

    @Test
    public void testGetBestBlockHash() throws IOException {
        BhpBlockHash getBestBlockHash = getBhpw3j().getBestBlockHash().send();
        String blockHash = getBestBlockHash.getBlockHash();
        assertNotNull(blockHash);
        assertThat(blockHash.length(), is(BLOCK_HASH_LENGTH_WITH_PREFIX));
    }

    @Test
    public void testGetBlockHash() throws IOException {
        BhpBlockHash getBestBlockHash = getBhpw3j().getBlockHash(new BlockParameterIndex(1)).send();
        String blockHash = getBestBlockHash.getBlockHash();
        assertNotNull(blockHash);
        assertThat(blockHash.length(), is(BLOCK_HASH_LENGTH_WITH_PREFIX));
    }

    @Test
    public void testGetConnectionCount() throws IOException {
        BhpConnectionCount getConnectionCount = getBhpw3j().getConnectionCount().send();
        Integer connectionCount = getConnectionCount.getCount();
        assertNotNull(connectionCount);
        assertThat(connectionCount, greaterThanOrEqualTo(0));
    }

    @Test
    public void testListAddress() throws IOException {
        BhpListAddress listAddress = getBhpw3j().listAddress().send();
        List<Address> addresses = listAddress.getAddresses();
        assertNotNull(addresses);
        assertThat(addresses, hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    public void testGetPeers() throws IOException {
        BhpGetPeers getPeers = getBhpw3j().getPeers().send();
        BhpGetPeers.Peers peers = getPeers.getPeers();
        assertNotNull(peers);
        assertThat(peers.getBad(), hasSize(greaterThanOrEqualTo(0)));
        assertThat(peers.getConnected(), hasSize(greaterThanOrEqualTo(0)));
        assertThat(peers.getUnconnected(), hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    public void testGetRawMemPool() throws IOException {
        BhpGetRawMemPool getRawMemPool = getBhpw3j().getRawMemPool().send();
        List<String> addresses = getRawMemPool.getAddresses();
        assertNotNull(addresses);
        assertThat(addresses, hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    public void testGetValidators() throws IOException {
        BhpGetValidators getValidators = getBhpw3j().getValidators().send();
        List<BhpGetValidators.Validator> validators = getValidators.getValidators();
        assertNotNull(validators);
        assertThat(validators, hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    public void testValidateAddress() throws IOException {
        BhpValidateAddress validateAddress = getBhpw3j().validateAddress(ADDRESS_1).send();
        BhpValidateAddress.Result validation = validateAddress.getValidation();
        assertNotNull(validation);
        assertThat(validation.getValid(), is(true));
        assertThat(validation.getAddress(), is(ADDRESS_1));
    }

    @Test
    public void testGetBlock_Index_fullTransactionObjects() throws IOException {
        BhpGetBlock bhpGetBlock = getBhpw3j()
                .getBlock(new BlockParameterIndex(BLOCK_2001_IDX), true)
                .send();
        BhpBlock block = bhpGetBlock.getBlock();
        assertNotNull(block);
        assertThat(block.getIndex(), is(BLOCK_2001_IDX));
        assertThat(block.getTransactions(), not(empty()));
    }

    @Test
    public void testGetBlock_Index() throws IOException {
        BhpGetBlock bhpGetBlock = getBhpw3j()
                .getBlock(new BlockParameterIndex(BLOCK_2001_IDX), false)
                .send();
        BhpBlock block = bhpGetBlock.getBlock();
        assertNotNull(block);
        assertThat(block.getIndex(), equalTo(BLOCK_2001_IDX));
        assertThat(block.getTransactions(), is(nullValue()));
    }

    @Test
    public void testGetRawBlock_Index() throws IOException {
        BhpGetRawBlock bhpGetRawBlock = getBhpw3j()
                .getRawBlock(new BlockParameterIndex(BLOCK_2001_IDX))
                .send();
        String rawBlock = bhpGetRawBlock.getRawBlock();
        assertThat(rawBlock, not(isEmptyOrNullString()));
    }

    @Test
    public void testGetBlock_Hash_fullTransactionObjects() throws IOException {
        BhpGetBlock bhpGetBlock = getBhpw3j()
                .getBlock(BLOCK_2001_HASH, true)
                .send();
        BhpBlock block = bhpGetBlock.getBlock();
        assertNotNull(block);
        assertThat(block.getIndex(), equalTo(BLOCK_2001_IDX));
        assertThat(block.getTransactions(), not(empty()));
    }

    @Test
    public void testGetBlock_Hash() throws IOException {
        BhpGetBlock bhpGetBlock = getBhpw3j()
                .getBlock(BLOCK_2001_HASH, false)
                .send();
        BhpBlock block = bhpGetBlock.getBlock();
        assertNotNull(block);
        assertThat(block.getIndex(), greaterThanOrEqualTo(BLOCK_2001_IDX));
        assertThat(block.getTransactions(), is(nullValue()));
    }

    @Test
    public void testGetRawBlock_Hash() throws IOException {
        BhpGetRawBlock bhpGetRawBlock = getBhpw3j()
                .getRawBlock(BLOCK_2001_HASH)
                .send();
        String rawBlock = bhpGetRawBlock.getRawBlock();
        assertThat(rawBlock, not(isEmptyOrNullString()));
    }

    @Test
    public void testGetBlockCount() throws Exception {
        BhpBlockCount bhpBlockCount = getBhpw3j().getBlockCount().send();
        BigInteger blockIndex = bhpBlockCount.getBlockIndex();
        assertNotNull(blockIndex);
        assertThat(bhpBlockCount.getBlockIndex(), greaterThan(BigInteger.valueOf(0)));
    }

    @Test
    public void testGetAccountState() throws IOException {
        BhpGetAccountState getAccountState = getBhpw3j()
                .getAccountState(ADDRESS_1).send();
        BhpGetAccountState.State accountState = getAccountState.getAccountState();
        assertNotNull(accountState);
        assertThat(accountState.getVotes(), is(empty()));
        assertThat(accountState.getFrozen(), is(false));
        assertThat(accountState.getVersion(), is(0));
        assertThat(accountState.getBalances(), hasSize(2));
        assertThat(accountState.getBalances(), hasItem(new BhpGetAccountState.Balance(prependHexPrefix(BhpAsset.HASH_ID), ADDR1_INIT_BHP_BALANCE)));
        assertThat(accountState.getBalances().get(1).getAssetAddress(), is(prependHexPrefix(GASAsset.HASH_ID)));
        assertThat(accountState.getBalances().get(1).getValue(), is(notNullValue()));
    }

    @Test
    public void testGetBlockHeader_Hash() throws IOException {
        BhpBlock block = getBhpw3j().getBlockHeader(BLOCK_2001_HASH).send().getBlock();
        assertThat(block, not(nullValue()));
        assertThat(block.getTransactions(), is(nullValue()));
        assertThat(block.getIndex(), is(notNullValue()));
        assertThat(block.getHash(), is(BLOCK_2001_HASH));
    }

    @Test
    public void testGetBlockHeader_Index() throws IOException {
        BhpBlock block = getBhpw3j().getBlockHeader(new BlockParameterIndex(BLOCK_2001_IDX)).send().getBlock();
        assertThat(block.getTransactions(), is(nullValue()));
        assertThat(block.getIndex(), is(BLOCK_2001_IDX));
        assertThat(block.getHash(), is(BLOCK_2001_HASH));
    }

    @Test
    public void testGetRawBlockHeader_Hash() throws IOException {
        BhpGetRawBlock getRawBlockHeader = getBhpw3j().getRawBlockHeader(BLOCK_2001_HASH).send();
        assertThat(getRawBlockHeader.getRawBlock(), is(notNullValue()));
        assertThat(getRawBlockHeader.getRawBlock(), is(BLOCK_2001_RAW_STRING));
    }

    @Test
    public void testGetRawBlockHeader_Index() throws IOException {
        BhpGetRawBlock getRawBlockHeader = getBhpw3j().getRawBlockHeader(new BlockParameterIndex(BLOCK_2001_IDX)).send();
        assertThat(getRawBlockHeader.getRawBlock(), is(notNullValue()));
        assertThat(getRawBlockHeader.getRawBlock(), is(BLOCK_2001_RAW_STRING));
    }

    @Test
    public void testGetWalletHeight() throws IOException {
        BhpGetWalletHeight getWalletHeight = getBhpw3j().getWalletHeight().send();
        BigInteger height = getWalletHeight.getHeight();
        assertNotNull(height);
        assertTrue(height.longValueExact() > 0);
    }

    @Test
    public void testGetBlockSysFee() throws IOException {
        BhpGetBlockSysFee getBlockSysFee = getBhpw3j()
                .getBlockSysFee(new BlockParameterIndex(BigInteger.ONE)).send();
        String fee = getBlockSysFee.getFee();
        assertThat(fee, not(isEmptyOrNullString()));
    }

    @Test
    public void testGetTxOut() throws IOException {
        BhpGetTxOut getTxOut = getBhpw3j()
                .getTxOut(UTXO_TX_HASH, 0)
                .send();
        TransactionOutput tx = getTxOut.getTransaction();
        assertNotNull(tx);
        assertThat(tx.getIndex(), is(0));
        assertThat(tx.getAssetId(), is(prependHexPrefix(BhpAsset.HASH_ID)));
        assertThat(tx.getAddress(), not(isEmptyOrNullString()));
        assertThat(tx.getValue(), not(isEmptyOrNullString()));
    }

    @Test
    public void testGetTransaction() throws IOException {
        BhpGetTransaction getTransaction = getBhpw3j().getTransaction(UTXO_TX_HASH).send();
        assertThat(getTransaction.getTransaction(), is(notNullValue()));
        assertThat(
                getTransaction.getTransaction().getTransactionId(),
                is(UTXO_TX_HASH)
        );
        assertThat(
                getTransaction.getTransaction().getSize(),
                is(223L)
        );
        assertThat(
                getTransaction.getTransaction().getType(),
                is(TransactionType.CONTRACT_TRANSACTION)
        );
        assertThat(
                getTransaction.getTransaction().getVersion(),
                is(0)
        );
        assertThat(
                getTransaction.getTransaction().getAttributes(),
                hasItem(
                        new TransactionAttribute(TransactionAttributeUsageType.SCRIPT, ADDR1_SCRIPT_HASH)
                )
        );
        assertThat(
                getTransaction.getTransaction().getOutputs(),
                hasItems(
                        new TransactionOutput(0, prependHexPrefix(BhpAsset.HASH_ID), ADDR1_INIT_BHP_BALANCE, ADDRESS_1)
                )
        );
        assertThat(
                getTransaction.getTransaction().getInputs(),
                hasItem(
                        new TransactionInput("0x83df8bd085fcb60b2789f7d0a9f876e5f3908567f7877fcba835e899b9dea0b5", 0)
                )
        );
        assertThat(
                getTransaction.getTransaction().getSysFee(),
                is("0")
        );
        assertThat(
                getTransaction.getTransaction().getNetFee(),
                is("0")
        );
        assertThat(
                getTransaction.getTransaction().getScripts(),
                hasItems(
                        new Script("40a3799c78dec17823fde75233793a7039bf2b1dbca4383a6eef1ac829460ba14c1e6a50ab1f2174e689bebfc0bb7accc965a6fe3e46d517b317bba1325b7fdaca", "21031a6c6fbbdf02ca351745fa86b9ba5a9452d785ac4f7fc2b7548ca2a46c4fcf4aac")
                )
        );
        assertThat(
                getTransaction.getTransaction().getBlockHash(),
                is(BLOCK_2008_HASH)
        );
        assertThat(
                getTransaction.getTransaction().getConfirmations(),
                greaterThanOrEqualTo(1L)
        );
        assertThat(
                getTransaction.getTransaction().getBlockTime(),
                greaterThanOrEqualTo(1547956859L)
        );
    }

    @Test
    public void testGetRawTransaction() throws IOException {
        BhpGetRawTransaction getRawTransaction = getBhpw3j().getRawTransaction(UTXO_TX_HASH).send();
        assertThat(getRawTransaction.getRawTransaction(), is("8000012023ba2703c53263e8d6e522dc32203339dcd8eee901b5a0deb999e835a8cb7f87f7678590f3e576f8a9d0f789270bb6fc85d08bdf830000019b7cffdaa674beae0f930ebe6085af9093e5fe56b34a5c220ccdcf6efc336fc50000c16ff286230023ba2703c53263e8d6e522dc32203339dcd8eee9014140a3799c78dec17823fde75233793a7039bf2b1dbca4383a6eef1ac829460ba14c1e6a50ab1f2174e689bebfc0bb7accc965a6fe3e46d517b317bba1325b7fdaca2321031a6c6fbbdf02ca351745fa86b9ba5a9452d785ac4f7fc2b7548ca2a46c4fcf4aac"));
    }

    @Test
    public void testGetBalance() throws IOException {
        BhpGetBalance getBalance = getBhpw3j().getBalance(BhpAsset.HASH_ID).send();
        assertThat(getBalance.getBalance(), is(notNullValue()));
        assertThat(Integer.parseInt(getBalance.getBalance().getConfirmed()), is(greaterThanOrEqualTo(0)));
        assertThat(Integer.parseInt(getBalance.getBalance().getConfirmed()), is(lessThanOrEqualTo(TOTAL_BHP_SUPPLY)));
        assertThat(Integer.parseInt(getBalance.getBalance().getBalance()), is(greaterThanOrEqualTo(0)));
        assertThat(Integer.parseInt(getBalance.getBalance().getBalance()), is(lessThanOrEqualTo(TOTAL_BHP_SUPPLY)));
    }

    @Test
    public void testGetAssetState() throws IOException {
        BhpGetAssetState getAssetState = getBhpw3j().getAssetState(BhpAsset.HASH_ID).send();
        assertThat(getAssetState.getAssetState(), CoreMatchers.is(CoreMatchers.notNullValue()));
        assertThat(
                getAssetState.getAssetState().getVersion(),
                is(0)
        );
        assertThat(
                getAssetState.getAssetState().getId(),
                is("0x" + BhpAsset.HASH_ID)
        );
        assertThat(
                getAssetState.getAssetState().getType(),
                is(AssetType.GOVERNING_TOKEN)
        );
        assertThat(
                getAssetState.getAssetState().getNames(),
                hasItems(
                        new BhpGetAssetState.AssetName("en", "AntShare")
                )
        );
        assertThat(
                getAssetState.getAssetState().getAmount(),
                is(Integer.toString(TOTAL_BHP_SUPPLY))
        );
        assertThat(
                getAssetState.getAssetState().getAvailable(),
                is(Integer.toString(TOTAL_BHP_SUPPLY))
        );
        assertThat(
                getAssetState.getAssetState().getPrecision(),
                is(0)
        );
        assertThat(
                getAssetState.getAssetState().getFee(),
                is(IsNull.nullValue())
        );
        assertThat(
                getAssetState.getAssetState().getAddress(),
                is(IsNull.nullValue())
        );
        assertThat(
                getAssetState.getAssetState().getOwner(),
                is("00")
        );
        assertThat(
                getAssetState.getAssetState().getAdmin(),
                is(ASSET_ISSUER_ADDRESS)
        );
        assertThat(
                getAssetState.getAssetState().getIssuer(),
                is(ASSET_ISSUER_ADDRESS)
        );
        assertThat(
                getAssetState.getAssetState().getExpiration(),
                is(4000000L)
        );
        assertThat(
                getAssetState.getAssetState().getFrozen(),
                is(false)
        );
    }

    @Test
    public void testDumpPrivKey() throws IOException {
        BhpDumpPrivKey bhpDumpPrivKey = getBhpw3j().dumpPrivKey(ADDRESS_1).send();
        String privKey = bhpDumpPrivKey.getDumpPrivKey();
        assertThat(privKey, not(isEmptyOrNullString()));
        assertThat(privKey, is(ADDR1_WIF));
    }

    @Test
    public void testGetStorage() throws IOException {
        // TODO: 2019-02-28 Guil:
        // to be implemented
    }

    @Test
    public void testGetContractState() throws IOException {
        // TODO: 2019-03-17 Guil:
        // to be implemented
    }

    @Test
    public void testGetUnspents() throws IOException {
        BhpGetUnspents response = getBhpw3j().getUnspents(ADDRESS_1).send();
        Unspents unspents = response.getUnspents();
        assertThat(unspents, is(notNullValue()));
        assertThat(unspents.getAddress(), is(ADDRESS_1));
        List<Balance> balances = unspents.getBalances();
        assertThat(balances, not(nullValue()));
        assertThat(balances.size(), is(2));

        Balance b = balances.get(0);
        assertThat(b.getAssetHash(), is(GASAsset.HASH_ID));
        assertThat(b.getAmount(), is(greaterThanOrEqualTo(BigDecimal.ZERO)));

        List<BhpGetUnspents.UnspentTransaction> utxos = b.getUnspentTransactions();
        assertThat(utxos, not(empty()));

        BhpGetUnspents.UnspentTransaction utxo = utxos.get(0);
        assertThat(utxo.getTxId(), not(isEmptyOrNullString()));
        assertThat(utxo.getIndex(), is(greaterThanOrEqualTo(0)));
        assertThat(utxo.getValue(), is(greaterThanOrEqualTo(BigDecimal.ZERO)));

        b = balances.get(1);
        assertThat(b.getAmount(), is(greaterThanOrEqualTo(BigDecimal.ZERO)));

        utxos = b.getUnspentTransactions();
        assertThat(utxos, not(empty()));

        utxo = utxos.get(0);
        assertThat(utxo.getTxId(), not(isEmptyOrNullString()));
        assertThat(utxo.getIndex(), is(greaterThanOrEqualTo(0)));
        assertThat(utxo.getValue(), is(greaterThanOrEqualTo(BigDecimal.ZERO)));
    }

    @Test
    public void testGetBrc5Balances() throws IOException {
        // TODO 2019-08-08 claude:
        // Implement
    }

    @Test
    public void testGetClaimable() throws IOException {
        // TODO: 2019-05-31 Claude:
        // Implement
    }

    @Test
    public void testListInputs() throws IOException {
        // TODO: 2019-06-12 Claude:
        // Implement
    }

}
