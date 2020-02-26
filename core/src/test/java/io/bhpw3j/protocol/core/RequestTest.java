package io.bhpw3j.protocol.core;

import io.bhpw3j.contract.ContractParameter;
import io.bhpw3j.contract.ScriptHash;
import io.bhpw3j.protocol.Bhpw3j;
import io.bhpw3j.protocol.RequestTester;
import io.bhpw3j.protocol.core.methods.response.TransactionOutput;
import io.bhpw3j.protocol.http.HttpService;
import org.junit.Test;

import java.util.Arrays;

public class RequestTest extends RequestTester {

    private Bhpw3j bhpw3J;

    @Override
    protected void initWeb3Client(HttpService httpService) {
        bhpw3J = Bhpw3j.build(httpService);
    }

    @Test
    public void testGetVersion() throws Exception {
        bhpw3J.getVersion().send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getversion\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testGetBestBlockHash() throws Exception {
        bhpw3J.getBestBlockHash().send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getbestblockhash\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testGetBlockHash() throws Exception {
        bhpw3J.getBlockHash(new BlockParameterIndex(16293)).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getblockhash\","
                        + "\"params\":[16293],\"id\":1}");
    }

    @Test
    public void testGetConnectionCount() throws Exception {
        bhpw3J.getConnectionCount().send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getconnectioncount\","
                        + "\"params\":[],\"id\":1}");
    }

    @Test
    public void testListAddress() throws Exception {
        bhpw3J.listAddress().send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"listaddress\","
                        + "\"params\":[],\"id\":1}");
    }

    @Test
    public void testGetPeers() throws Exception {
        bhpw3J.getPeers().send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getpeers\","
                        + "\"params\":[],\"id\":1}");
    }

    @Test
    public void testGetRawMemPool() throws Exception {
        bhpw3J.getRawMemPool().send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getrawmempool\","
                        + "\"params\":[],\"id\":1}");
    }

    @Test
    public void testGetValidators() throws Exception {
        bhpw3J.getValidators().send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getvalidators\","
                        + "\"params\":[],\"id\":1}");
    }

    @Test
    public void testValidateAddress() throws Exception {
        bhpw3J.validateAddress("AQVh2pG732YvtNaxEGkQUei3YA4cvo7d2i").send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"validateaddress\","
                        + "\"params\":[\"AQVh2pG732YvtNaxEGkQUei3YA4cvo7d2i\"],\"id\":1}");
    }

    @Test
    public void testGetBlock_Index() throws Exception {
        bhpw3J.getBlock(new BlockParameterIndex(12345), true).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getblock\","
                        + "\"params\":[12345,1],\"id\":1}");
    }

    @Test
    public void testGetRawBlock_Index() throws Exception {
        bhpw3J.getRawBlock(new BlockParameterIndex(12345)).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getblock\","
                        + "\"params\":[12345,0],\"id\":1}");
    }

    @Test
    public void testGetBlockCount() throws Exception {
        bhpw3J.getBlockCount().send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getblockcount\","
                        + "\"params\":[],\"id\":1}");
    }

    @Test
    public void testGetBlock_Hash() throws Exception {
        bhpw3J.getBlock("0x2240b34669038f82ac492150d391dfc3d7fe5e3c1d34e5b547d50e99c09b468d", true).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getblock\","
                        + "\"params\":[\"0x2240b34669038f82ac492150d391dfc3d7fe5e3c1d34e5b547d50e99c09b468d\",1],\"id\":1}");
    }

    @Test
    public void testGetRawBlock_Hash() throws Exception {
        bhpw3J.getRawBlock("0x2240b34669038f82ac492150d391dfc3d7fe5e3c1d34e5b547d50e99c09b468d").send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getblock\","
                        + "\"params\":[\"0x2240b34669038f82ac492150d391dfc3d7fe5e3c1d34e5b547d50e99c09b468d\",0],\"id\":1}");
    }

    @Test
    public void testGetAccountState() throws Exception {
        bhpw3J.getAccountState("AQVh2pG732YvtNaxEGkQUei3YA4cvo7d2i").send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getaccountstate\","
                        + "\"params\":[\"AQVh2pG732YvtNaxEGkQUei3YA4cvo7d2i\"],\"id\":1}");
    }

    @Test
    public void testGetBlockHeader_Hash() throws Exception {
        bhpw3J.getBlockHeader("0x2240b34669038f82ac492150d391dfc3d7fe5e3c1d34e5b547d50e99c09b468d").send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getblockheader\","
                        + "\"params\":[\"0x2240b34669038f82ac492150d391dfc3d7fe5e3c1d34e5b547d50e99c09b468d\",1],\"id\":1}");
    }

    @Test
    public void testGetBlockHeader_Index() throws Exception {
        bhpw3J.getBlockHeader(new BlockParameterIndex(12345)).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getblockheader\","
                        + "\"params\":[12345,1],\"id\":1}");
    }

    @Test
    public void testGetRawBlockHeader_Hash() throws Exception {
        bhpw3J.getRawBlockHeader("0x2240b34669038f82ac492150d391dfc3d7fe5e3c1d34e5b547d50e99c09b468d").send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getblockheader\","
                        + "\"params\":[\"0x2240b34669038f82ac492150d391dfc3d7fe5e3c1d34e5b547d50e99c09b468d\",0],\"id\":1}");
    }

    @Test
    public void testGetRawBlockHeader_Index() throws Exception {
        bhpw3J.getRawBlockHeader(new BlockParameterIndex(12345)).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getblockheader\","
                        + "\"params\":[12345,0],\"id\":1}");
    }

    @Test
    public void testGetNewAddress() throws Exception {
        bhpw3J.getNewAddress().send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getnewaddress\","
                        + "\"params\":[],\"id\":1}");
    }

    @Test
    public void testGetWalletHeight() throws Exception {
        bhpw3J.getWalletHeight().send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getwalletheight\","
                        + "\"params\":[],\"id\":1}");
    }

    @Test
    public void testGetBlockSysFee() throws Exception {
        bhpw3J.getBlockSysFee(new BlockParameterIndex(12345)).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getblocksysfee\","
                        + "\"params\":[12345],\"id\":1}");
    }

    @Test
    public void testGetTxOut() throws Exception {
        bhpw3J.getTxOut("0x93c569cbe33e918f7a5392025fbdeab5f6c97c8e5897fafc466694b6e8e1b0d2", 0).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"gettxout\","
                        + "\"params\":[\"0x93c569cbe33e918f7a5392025fbdeab5f6c97c8e5897fafc466694b6e8e1b0d2\",0],\"id\":1}");
    }

    @Test
    public void testSendRawTransaction() throws Exception {
        bhpw3J.sendRawTransaction("80000001d405ab03e736a01ca277d94b1377113c7e961bb4550511fe1d408f30c77a82650000029b7cffdaa674beae0f930ebe6085af9093e5fe56b34a5c220ccdcf6efc336fc500ca9a3b0000000023ba2703c53263e8d6e522dc32203339dcd8eee99b7cffdaa674beae0f930ebe6085af9093e5fe56b34a5c220ccdcf6efc336fc5001a711802000000295f83f83fc439f56e6e1fb062d89c6f538263d70141403711e366fc99e77a110b6c96b5f8828ef956a6d5cfa5cb63273419149011b0f30dc5458faa59e4867d0ac7537e324c98124bb691feca5c5ddf6ed20f4adb778223210265bf906bf385fbf3f777832e55a87991bcfbe19b097fb7c5ca2e4025a4d5e5d6ac").send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"sendrawtransaction\","
                        + "\"params\":[\"80000001d405ab03e736a01ca277d94b1377113c7e961bb4550511fe1d408f30c77a82650000029b7cffdaa674beae0f930ebe6085af9093e5fe56b34a5c220ccdcf6efc336fc500ca9a3b0000000023ba2703c53263e8d6e522dc32203339dcd8eee99b7cffdaa674beae0f930ebe6085af9093e5fe56b34a5c220ccdcf6efc336fc5001a711802000000295f83f83fc439f56e6e1fb062d89c6f538263d70141403711e366fc99e77a110b6c96b5f8828ef956a6d5cfa5cb63273419149011b0f30dc5458faa59e4867d0ac7537e324c98124bb691feca5c5ddf6ed20f4adb778223210265bf906bf385fbf3f777832e55a87991bcfbe19b097fb7c5ca2e4025a4d5e5d6ac\"],\"id\":1}");
    }

    @Test
    public void testSendToAddress() throws Exception {
        bhpw3J.sendToAddress(
                "c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b",
                "AKYdmtzCD6DtGx16KHzSTKY8ji29sMTbEZ",
                "10.0"
        ).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"sendtoaddress\","
                        + "\"params\":[\"c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b\","
                        + "\"AKYdmtzCD6DtGx16KHzSTKY8ji29sMTbEZ\",\"10.0\"],\"id\":1}");
    }

    @Test
    public void testSendToAddress_Fee() throws Exception {
        bhpw3J.sendToAddress(
                "c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b",
                "AKYdmtzCD6DtGx16KHzSTKY8ji29sMTbEZ",
                "10.0",
                "0.01"
        ).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"sendtoaddress\","
                        + "\"params\":[\"c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b\","
                        + "\"AKYdmtzCD6DtGx16KHzSTKY8ji29sMTbEZ\","
                        + "\"10.0\","
                        + "\"0.01\"],\"id\":1}");
    }

    @Test
    public void testSendToAddress_Fee_And_ChangeAddress() throws Exception {
        bhpw3J.sendToAddress(
                "c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b",
                "AKYdmtzCD6DtGx16KHzSTKY8ji29sMTbEZ",
                "10.0",
                "0.01",
                "AK2nJJpJr6o664CWJKi1QRXjqeic2zRp8y"
        ).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"sendtoaddress\","
                        + "\"params\":[\"c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b\","
                        + "\"AKYdmtzCD6DtGx16KHzSTKY8ji29sMTbEZ\","
                        + "\"10.0\","
                        + "\"0.01\","
                        + "\"AK2nJJpJr6o664CWJKi1QRXjqeic2zRp8y\"],\"id\":1}");
    }

    @Test
    public void testGetTransaction() throws Exception {
        bhpw3J.getTransaction("0x1f31821787b0a53df0ff7d6e0e7ecba3ac19dd517d6d2ea5aaf00432c20831d6").send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getrawtransaction\","
                        + "\"params\":[\"0x1f31821787b0a53df0ff7d6e0e7ecba3ac19dd517d6d2ea5aaf00432c20831d6\",1],\"id\":1}");
    }

    @Test
    public void testGetRawTransaction() throws Exception {
        bhpw3J.getRawTransaction("0x1f31821787b0a53df0ff7d6e0e7ecba3ac19dd517d6d2ea5aaf00432c20831d6").send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getrawtransaction\","
                        + "\"params\":[\"0x1f31821787b0a53df0ff7d6e0e7ecba3ac19dd517d6d2ea5aaf00432c20831d6\",0],\"id\":1}");
    }

    @Test
    public void testGetBalance() throws Exception {
        bhpw3J.getBalance("c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b").send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getbalance\","
                        + "\"params\":[\"c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b\"],\"id\":1}");
    }

    @Test
    public void testGetBalance_with_Prefix() throws Exception {
        bhpw3J.getBalance("0xc56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b").send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getbalance\","
                        + "\"params\":[\"c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b\"],\"id\":1}");
    }

    @Test
    public void testGetAssetState() throws Exception {
        bhpw3J.getAssetState("c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b").send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getassetstate\","
                        + "\"params\":[\"c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b\"],\"id\":1}");
    }

    @Test
    public void testGetAssetState_with_Prefix() throws Exception {
        bhpw3J.getAssetState("0xc56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b").send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getassetstate\","
                        + "\"params\":[\"c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b\"],\"id\":1}");
    }

    @Test
    public void testSendMany() throws Exception {
        bhpw3J.sendMany(
                Arrays.asList(
                        new TransactionOutput("c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b", "100", "AbRTHXb9zqdqn5sVh4EYpQHGZ536FgwCx2"),
                        new TransactionOutput("c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b", "10", "AbRTHXb9zqdqn5sVh4EYpQHGZ536FgwCx2")
                )
        ).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"sendmany\","
                        + "\"params\":["
                        + "["
                        + "{\"asset\":\"c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b\",\"value\":\"100\",\"address\":\"AbRTHXb9zqdqn5sVh4EYpQHGZ536FgwCx2\"},"
                        + "{\"asset\":\"c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b\",\"value\":\"10\",\"address\":\"AbRTHXb9zqdqn5sVh4EYpQHGZ536FgwCx2\"}"
                        + "]"
                        + "],\"id\":1}");
    }

    @Test
    public void testSendMany_Empty_Transaction() throws Exception {
        bhpw3J.sendMany(Arrays.asList()).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"sendmany\","
                        + "\"params\":[[]],\"id\":1}");
    }

    @Test
    public void testSendMany_Fee() throws Exception {
        bhpw3J.sendMany(
                Arrays.asList(
                        new TransactionOutput("c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b", "100", "AbRTHXb9zqdqn5sVh4EYpQHGZ536FgwCx2"),
                        new TransactionOutput("c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b", "10", "AbRTHXb9zqdqn5sVh4EYpQHGZ536FgwCx2")
                ),
                "50"
        ).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"sendmany\","
                        + "\"params\":["
                        + "["
                        + "{\"asset\":\"c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b\",\"value\":\"100\",\"address\":\"AbRTHXb9zqdqn5sVh4EYpQHGZ536FgwCx2\"},"
                        + "{\"asset\":\"c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b\",\"value\":\"10\",\"address\":\"AbRTHXb9zqdqn5sVh4EYpQHGZ536FgwCx2\"}"
                        + "],"
                        + "\"50\""
                        + "],\"id\":1}");
    }

    @Test
    public void testSendMany_Fee_And_ChangeAddress() throws Exception {
        bhpw3J.sendMany(
                Arrays.asList(
                        new TransactionOutput("c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b", "100", "AbRTHXb9zqdqn5sVh4EYpQHGZ536FgwCx2"),
                        new TransactionOutput("c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b", "10", "AbRTHXb9zqdqn5sVh4EYpQHGZ536FgwCx2")
                ),
                "50",
                "AK2nJJpJr6o664CWJKi1QRXjqeic2zRp8y"
        ).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"sendmany\","
                        + "\"params\":["
                        + "["
                        + "{\"asset\":\"c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b\",\"value\":\"100\",\"address\":\"AbRTHXb9zqdqn5sVh4EYpQHGZ536FgwCx2\"},"
                        + "{\"asset\":\"c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b\",\"value\":\"10\",\"address\":\"AbRTHXb9zqdqn5sVh4EYpQHGZ536FgwCx2\"}"
                        + "],"
                        + "\"50\","
                        + "\"AK2nJJpJr6o664CWJKi1QRXjqeic2zRp8y\""
                        + "],\"id\":1}");
    }

    @Test
    public void testDumpPrivKey() throws Exception {
        bhpw3J.dumpPrivKey("ARye5QEj8YX2vpJ297Lrcmz9m6F8hadgxg").send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"dumpprivkey\","
                        + "\"params\":[\"ARye5QEj8YX2vpJ297Lrcmz9m6F8hadgxg\"],\"id\":1}");
    }

    @Test
    public void testGetStorage() throws Exception {
        bhpw3J.getStorage("03febccf81ac85e3d795bc5cbd4e84e907812aa3", "616e797468696e67").send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getstorage\","
                        + "\"params\":[\"03febccf81ac85e3d795bc5cbd4e84e907812aa3\",\"616e797468696e67\"],\"id\":1}");
    }

    @Test
    public void testGetStorage_with_HexParameter() throws Exception {
        bhpw3J.getStorage("03febccf81ac85e3d795bc5cbd4e84e907812aa3", HexParameter.valueOf("anything")).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getstorage\","
                        + "\"params\":[\"03febccf81ac85e3d795bc5cbd4e84e907812aa3\",\"616e797468696e67\"],\"id\":1}");
    }

    @Test
    public void testInvoke() throws Exception {
        bhpw3J.invoke(
                "dc675afc61a7c0f7b3d2682bf6e1d8ed865a0e5f",
                Arrays.asList(
                        ContractParameter.signature("53c874d7c434b9912b9ee38b958ec78c1c4b0a3c4b5753bada198a1e49649f13bf5def112ee8d31133799759d3d88dd3c1650a4d6fa36f29493ffbc8068600ed"),
                        ContractParameter.bool(false),
                        ContractParameter.integer(8),
                        ContractParameter.hash160(new ScriptHash("576f6f6c6f576f6f6c6f576f6f6c6f576f6f6c6f")),
                        ContractParameter.hash256(new ScriptHash("576f6f6c6f576f6f6c6f576f6f6c6f576f6f6c6ff6c6f576f6f6c6f576f6f6cf")),
                        ContractParameter.byteArray("4e45503520474153"),
                        ContractParameter.string("name"),
                        ContractParameter.array(
                                ContractParameter.string("name"),
                                ContractParameter.byteArray("4e45503520474153"),
                                ContractParameter.array(
                                        ContractParameter.string("name")
                                )
                        )
                        // TODO 17.07.19 claude:
                        // Include public key parameter when it is implemented.
                        // ContractParameter.publicKey("4e45503520474153"),

                        // INTEROP_INTERFACE and VOID are only return types and need not be tested here.
                        // ContractParameter(ContractParameterType.INTEROP_INTERFACE, "array"),
                        // ContractParameter(ContractParameterType.VOID, "")
                )
        ).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"invoke\","
                        + "\"params\":[\"dc675afc61a7c0f7b3d2682bf6e1d8ed865a0e5f\","
                        + "["
                        +   "{\"type\":\"Signature\",\"value\":\"53c874d7c434b9912b9ee38b958ec78c1c4b0a3c4b5753bada198a1e49649f13bf5def112ee8d31133799759d3d88dd3c1650a4d6fa36f29493ffbc8068600ed\"},"
                        +   "{\"type\":\"Boolean\",\"value\":false},"
                        +   "{\"type\":\"Integer\",\"value\":\"8\"},"
                        +   "{\"type\":\"Hash160\",\"value\":\"576f6f6c6f576f6f6c6f576f6f6c6f576f6f6c6f\"},"
                        +   "{\"type\":\"Hash256\",\"value\":\"576f6f6c6f576f6f6c6f576f6f6c6f576f6f6c6ff6c6f576f6f6c6f576f6f6cf\"},"
                        +   "{\"type\":\"ByteArray\",\"value\":\"4e45503520474153\"},"
                        +   "{\"type\":\"String\",\"value\":\"name\"},"
                        +   "{\"type\":\"Array\",\"value\":"
                        +       "["
                        +           "{\"type\":\"String\",\"value\":\"name\"},"
                        +           "{\"type\":\"ByteArray\",\"value\":\"4e45503520474153\"},"
                        +           "{\"type\":\"Array\",\"value\":"
                        +               "["
                        +                   "{\"type\":\"String\",\"value\":\"name\"}"
                        +               "]"
                        +           "}"
                        +       "]"
                        +   "}"
                        + "]"
                        + "],\"id\":1}"
//                        + "{\"type\":\"PublicKey\",\"value\":\"4e45503520474153\"},"
        );
    }

    @Test
    public void testInvoke_empty_Params() throws Exception {
        bhpw3J.invoke(
                "dc675afc61a7c0f7b3d2682bf6e1d8ed865a0e5f",
                Arrays.asList()
        ).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"invoke\","
                        + "\"params\":[\"dc675afc61a7c0f7b3d2682bf6e1d8ed865a0e5f\","
                        + "[]"
                        + "],\"id\":1}"
        );
    }

    @Test
    public void testInvokeFunction() throws Exception {
        bhpw3J.invokeFunction(
                "af7c7328eee5a275a3bcaee2bf0cf662b5e739be",
                "balanceOf",
                Arrays.asList(
                        ContractParameter.hash160(new ScriptHash("91b83e96f2a7c4fdf0c1688441ec61986c7cae26"))
                )
        ).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"invokefunction\","
                        + "\"params\":[\"af7c7328eee5a275a3bcaee2bf0cf662b5e739be\","
                        + "\"balanceOf\","
                        + "["
                        + "{\"type\":\"Hash160\",\"value\":\"91b83e96f2a7c4fdf0c1688441ec61986c7cae26\"}"
                        + "]"
                        + "],\"id\":1}"
        );
    }

    @Test
    public void testInvokeFunction_without_Params() throws Exception {
        bhpw3J.invokeFunction(
                "af7c7328eee5a275a3bcaee2bf0cf662b5e739be",
                "balanceOf"
        ).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"invokefunction\","
                        + "\"params\":[\"af7c7328eee5a275a3bcaee2bf0cf662b5e739be\","
                        + "\"balanceOf\""
                        + "],\"id\":1}"
        );
    }

    @Test
    public void testInvokeFunction_empty_Params() throws Exception {
        bhpw3J.invokeFunction(
                "af7c7328eee5a275a3bcaee2bf0cf662b5e739be",
                "balanceOf",
                Arrays.asList()
        ).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"invokefunction\","
                        + "\"params\":[\"af7c7328eee5a275a3bcaee2bf0cf662b5e739be\","
                        + "\"balanceOf\","
                        + "[]"
                        + "],\"id\":1}"
        );
    }

    @Test
    public void testInvokeScript() throws Exception {
        bhpw3J.invokeScript("00046e616d656724058e5e1b6008847cd662728549088a9ee82191").send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"invokescript\","
                        + "\"params\":[\"00046e616d656724058e5e1b6008847cd662728549088a9ee82191\"],\"id\":1}"
        );
    }

    @Test
    public void testGetContractState() throws Exception {
        bhpw3J.getContractState("dc675afc61a7c0f7b3d2682bf6e1d8ed865a0e5f").send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getcontractstate\","
                        + "\"params\":[\"dc675afc61a7c0f7b3d2682bf6e1d8ed865a0e5f\"],\"id\":1}"
        );
    }

    @Test
    public void testSubmitBlock() throws Exception {
        bhpw3J.submitBlock("00000000000000000000000000000000").send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"submitblock\","
                        + "\"params\":[\"00000000000000000000000000000000\"],\"id\":1}"
        );
    }

    @Test
    public void testGetUnspents() throws Exception {
        bhpw3J.getUnspents("AK2nJJpJr6o664CWJKi1QRXjqeic2zRp8y").send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getunspents\","
                        + "\"params\":[\"AK2nJJpJr6o664CWJKi1QRXjqeic2zRp8y\"],\"id\":1}"
        );
    }

    @Test
    public void testBrc5Balances() throws Exception {
        bhpw3J.getBrc5Balances("AK2nJJpJr6o664CWJKi1QRXjqeic2zRp8y").send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getbrc5balances\","
                        + "\"params\":[\"AK2nJJpJr6o664CWJKi1QRXjqeic2zRp8y\"],\"id\":1}"
        );
    }

    @Test
    public void testGetClaimable() throws Exception {
        bhpw3J.getClaimable("AK2nJJpJr6o664CWJKi1QRXjqeic2zRp8y").send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getclaimable\","
                        + "\"params\":[\"AK2nJJpJr6o664CWJKi1QRXjqeic2zRp8y\"],\"id\":1}"
        );
    }

    @Test
    public void testGetApplicationLog() throws Exception {
        bhpw3J.getApplicationLog("420d1eb458c707d698c6d2ba0f91327918ddb3b7bae2944df070f3f4e579078b").send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"getapplicationlog\","
                        + "\"params\":[\"420d1eb458c707d698c6d2ba0f91327918ddb3b7bae2944df070f3f4e579078b\"],\"id\":1}"
        );
    }

    @Test
    public void testListPlugins() throws Exception {
        bhpw3J.listPlugins().send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"listplugins\",\"params\":[],\"id\":1}"
        );
    }
}
