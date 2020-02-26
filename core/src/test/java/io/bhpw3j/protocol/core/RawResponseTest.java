package io.bhpw3j.protocol.core;

import io.bhpw3j.protocol.ResponseTester;
import io.bhpw3j.protocol.core.methods.response.BhpGetVersion;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Raw Response tests.
 */
public class RawResponseTest extends ResponseTester {

    private static final String RAW_RESPONSE = "{\n"
            + "  \"id\":67,\n"
            + "  \"jsonrpc\":\"2.0\",\n"
            + "  \"result\": { "
            + "    \"port\": 1234,\n"
            + "    \"nonce\": 12345678,\n"
            + "    \"useragent\": \"\\/BHP:2.7.6\\/\"\n"
            + "   }"
            + "}";

    @Test
    public void testRawResponseEnabled() {
        configureWeb3Service(true);
        final BhpGetVersion web3ClientVersion = deserialiseWeb3ClientVersionResponse();
        assertThat(web3ClientVersion.getRawResponse(), is(RAW_RESPONSE));
    }

    @Test
    public void testRawResponseDisabled() {
        configureWeb3Service(false);
        final BhpGetVersion web3ClientVersion = deserialiseWeb3ClientVersionResponse();
        assertThat(web3ClientVersion.getRawResponse(), nullValue());
    }

    private BhpGetVersion deserialiseWeb3ClientVersionResponse() {
        buildResponse(RAW_RESPONSE);

        return deserialiseResponse(BhpGetVersion.class);
    }
}
