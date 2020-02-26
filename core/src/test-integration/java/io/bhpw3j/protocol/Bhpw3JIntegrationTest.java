package io.bhpw3j.protocol;

import io.bhpw3j.model.types.BhpAsset;
import io.bhpw3j.protocol.core.JsonRpc2_0Bhpw3J;
import io.bhpw3j.protocol.http.HttpService;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.testcontainers.containers.GenericContainer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

abstract class Bhpw3JIntegrationTest {

    protected static String PRIVNET_CONTAINER = "axlabs/bhp-privatenet-openwallet-docker:latest";

    // This is the port of one of the .NET nodes which is exposed internally by the container.
    protected static int EXPOSED_INTERNAL_PORT_BHP_DOTNET = 30333;

    protected static int BLOCK_HASH_LENGTH_WITH_PREFIX = 66;
    protected static final int TOTAL_BHP_SUPPLY = 100000000;

    protected static String ADDRESS_1 = "AK2nJJpJr6o664CWJKi1QRXjqeic2zRp8y";
    protected static String ADDRESS_2 = "AbRTHXb9zqdqn5sVh4EYpQHGZ536FgwCx2";
    protected static String ADDRESS_3 = "AKkkumHbBipZ46UMZJoFynJMXzSRnBvKcs";
    protected static String ADDRESS_4 = "AKYdmtzCD6DtGx16KHzSTKY8ji29sMTbEZ";

    // This address is the issuer of the global assets in the container's private net.
    protected static String ASSET_ISSUER_ADDRESS = "Abf2qMs1pzQb8kYk9RuxtUb9jtRKJVuBJt";

    protected static final String ADDR1_SCRIPT_HASH = "23ba2703c53263e8d6e522dc32203339dcd8eee9";
    protected static final String ADDR1_INIT_BHP_BALANCE = "100000000";
    protected static final String ADDR1_WIF = "KxDgvEKzgSBPPfuVfw67oPQBSjidEiqTHURKSDL1R7yGaGYAeYnr";

    protected static long BLOCK_2001_IDX = 2001;
    protected static String BLOCK_2001_HASH = "0x48df2ad40c6f4e0a051b341d6439ad537d284c9293662528710239fff1734286";
    protected static String BLOCK_2001_RAW_STRING = "00000000b2d9ec1d9b7fc1403c9f06b5359bb896c3e0fa7507f8403fe16b313ffaafde2fa657d2d7eefca1aed0a04ce3f04f4cc1aeef4c8ea3441a4925f9019530a67ff430c3375dd1070000365efd76ca2542d6be48d3a3f5d10013ab9ffee489706078714f1ea201c340ddb90a3c816359a1a2902cef39d6acffa11aa07d6e1029c350848734c96a57d9445cd39fa838076f54586587608dbddf3d8b61c4f655756d3195e67f3e7d12f64026a94fd4f05023dc622b507437d6478171d6062acd14798ef67e7bf353d149f29f466fad57bb1c80dbfc4c7aa7a7ebd88084e4673ffa9964d4faf4d81bc930d64008911ce30594e46da256aca0c900f48879899b6b4c47afc923edf31be82158a7b17b0d9106b2a6d1371f564850db2dc7843bbd04cbbedfd9c174faf986c7e2778b532102103a7f7dd016558597f7960d27c516a4394fd968b9e65155eb4b013e4040406e2102a7bc55fe8684e0119768d104ba30795bdcc86619e864add26156723ed185cd622102b3622bf4017bdfe317c58aed5f4c753f206b7db896046fa7d774bbc4bf7f8dc22103d90c07df63e690ce77912e10ab51acc944b66860237b608c4f8f8309e71ee69954ae00";

    protected static final String BLOCK_2008_HASH = "0x268c246bc4993b9f66c7dd030d6708d07c1f0271c1cd3027451389292732912c";

    // This is the last unspent transaction of address AK2nJJ... after a clean start of the container.
    protected static final String UTXO_TX_HASH = "0x4ba4d1f1acf7c6648ced8824aa2cd3e8f836f59e7071340e0c440d099a508cff";

    private Bhpw3JWrapper bhpw3jWrapper;

    @Before
    public void setUp() {
        bhpw3jWrapper = new Bhpw3JWrapper(new HttpService(getDotNetNodeUrl(getPrivateNetContainer())));
        bhpw3jWrapper.waitUntilWalletHasBalanceGreaterThanOrEqualToOne();
    }

    protected abstract GenericContainer getPrivateNetContainer();

    protected Bhpw3j getBhpw3j() {
        return bhpw3jWrapper;
    }

    protected static String getDotNetNodeUrl(GenericContainer container) {
        return "http://" + container.getContainerIpAddress() +
                ":" + container.getMappedPort(EXPOSED_INTERNAL_PORT_BHP_DOTNET);
    }

    protected static class Bhpw3JWrapper extends JsonRpc2_0Bhpw3J {

        private Bhpw3JWrapper(Bhpw3jService bhpw3JService) {
            super(bhpw3JService);
        }

        private void waitUntilWalletHasBalanceGreaterThanOrEqualToOne() {
            waitUntilWalletHasBalance(greaterThanOrEqualTo(BigDecimal.ONE));
        }

        private void waitUntilWalletHasBalance(Matcher matcher) {
            waitUntil(callableGetBalance(), matcher);
        }

        private void waitUntil(Callable<?> callable, Matcher matcher) {
            await().timeout(30, TimeUnit.SECONDS).until(callable, matcher);
        }

        private Callable<BigDecimal> callableGetBalance() {
            return () -> {
                try {
                    return new BigDecimal(super.getBalance(BhpAsset.HASH_ID).send()
                            .getBalance().getBalance());
                } catch (IOException e) {
                    return BigDecimal.ZERO;
                }
            };
        }

    }

}
