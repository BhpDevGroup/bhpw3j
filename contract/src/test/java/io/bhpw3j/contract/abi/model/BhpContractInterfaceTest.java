package io.bhpw3j.contract.abi.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.bhpw3j.contract.ContractParameter;
import io.bhpw3j.model.types.ContractParameterType;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class BhpContractInterfaceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testSerialize() throws JsonProcessingException {
        BhpContractInterface bhpContractInterface = new BhpContractInterface(
                "anything",
                "Main",
                Arrays.asList(
                        new BhpContractFunction("anything", Arrays.asList(ContractParameter.byteArray("001010101010")), ContractParameterType.BYTE_ARRAY)
                ),
                Arrays.asList(
                        new BhpContractEvent("anything", Arrays.asList(ContractParameter.byteArray("001010101010")))
                )
        );
        String bhpContractInterfaceString = objectMapper.writeValueAsString(bhpContractInterface);

        assertThat(bhpContractInterfaceString,
                is(
                        "{" +
                                "\"hash\":\"anything\"," +
                                "\"entrypoint\":\"Main\"," +
                                "\"functions\":[" +
                                "{" +
                                "\"name\":\"anything\"," +
                                "\"parameters\":[" +
                                "{" +
                                "\"type\":\"ByteArray\"," +
                                "\"value\":\"001010101010\"" +
                                "}" +
                                "]," +
                                "\"returntype\":\"ByteArray\"" +
                                "}" +
                                "]," +
                                "\"events\":[" +
                                "{" +
                                "\"name\":\"anything\"," +
                                "\"parameters\":[" +
                                "{" +
                                "\"type\":\"ByteArray\"," +
                                "\"value\":\"001010101010\"" +
                                "}" +
                                "]" +
                                "}" +
                                "]" +
                                "}"
                )
        );
    }

    @Test
    public void testSerialize_Empty() throws JsonProcessingException {
        BhpContractInterface bhpContractInterface = new BhpContractInterface(
                "anything",
                "Main",
                Arrays.asList(),
                Arrays.asList()
        );
        String bhpContractInterfaceString = objectMapper.writeValueAsString(bhpContractInterface);

        assertThat(bhpContractInterfaceString,
                is(
                        "{" +
                                "\"hash\":\"anything\"," +
                                "\"entrypoint\":\"Main\"," +
                                "\"functions\":[" +
                                "]," +
                                "\"events\":[" +
                                "]" +
                                "}"
                )
        );
    }

    @Test
    public void testSerialize_Null() throws JsonProcessingException {
        BhpContractInterface bhpContractInterface = new BhpContractInterface(
                "anything",
                "Main",
                null,
                null
        );
        String bhpContractInterfaceString = objectMapper.writeValueAsString(bhpContractInterface);

        assertThat(bhpContractInterfaceString,
                is(
                        "{" +
                                "\"hash\":\"anything\"," +
                                "\"entrypoint\":\"Main\"," +
                                "\"functions\":[" +
                                "]," +
                                "\"events\":[" +
                                "]" +
                                "}"
                )
        );
    }

    @Test
    public void testDeserialize() throws IOException {

        String bhpContractInterfaceString = "{" +
                "\"hash\":\"anything\"," +
                "\"entrypoint\":\"Main\"," +
                "\"functions\":[" +
                "{" +
                "\"name\":\"anything\"," +
                "\"parameters\":[" +
                "{" +
                "\"type\":\"ByteArray\"," +
                "\"value\":\"001010101010\"" +
                "}" +
                "]," +
                "\"returntype\":\"ByteArray\"" +
                "}" +
                "]," +
                "\"events\":[" +
                "{" +
                "\"name\":\"anything\"," +
                "\"parameters\":[" +
                "{" +
                "\"type\":\"ByteArray\"," +
                "\"value\":\"001010101010\"" +
                "}" +
                "]" +
                "}" +
                "]" +
                "}";

        BhpContractInterface bhpContractInterface = objectMapper.readValue(bhpContractInterfaceString, BhpContractInterface.class);

        assertThat(bhpContractInterface.getHash(), is("anything"));
        assertThat(bhpContractInterface.getEntryPoint(), is("Main"));
        assertThat(bhpContractInterface.getFunctions(), not(emptyCollectionOf(BhpContractFunction.class)));
        assertThat(bhpContractInterface.getFunctions(),
                CoreMatchers.hasItems(
                        new BhpContractFunction("anything", Arrays.asList(ContractParameter.byteArray("001010101010")), ContractParameterType.BYTE_ARRAY)
                )
        );
        assertThat(bhpContractInterface.getEvents(), not(emptyCollectionOf(BhpContractEvent.class)));
        assertThat(bhpContractInterface.getEvents(),
                CoreMatchers.hasItems(
                        new BhpContractEvent("anything", Arrays.asList(ContractParameter.byteArray("001010101010")))
                )
        );
    }

    @Test
    public void testDeserialize_Empty() throws IOException {

        String bhpContractInterfaceString = "{" +
                "\"hash\":\"anything\"," +
                "\"entrypoint\":\"Main\"," +
                "\"functions\":[" +
                "]," +
                "\"events\":[" +
                "]" +
                "}";

        BhpContractInterface bhpContractInterface = objectMapper.readValue(bhpContractInterfaceString, BhpContractInterface.class);

        assertThat(bhpContractInterface.getHash(), is("anything"));
        assertThat(bhpContractInterface.getEntryPoint(), is("Main"));
        assertThat(bhpContractInterface.getFunctions(), emptyCollectionOf(BhpContractFunction.class));
        assertThat(bhpContractInterface.getFunctions(), hasSize(0));
        assertThat(bhpContractInterface.getEvents(), emptyCollectionOf(BhpContractEvent.class));
        assertThat(bhpContractInterface.getEvents(), hasSize(0));
    }

    @Test
    public void testDeserialize_Null() throws IOException {

        String bhpContractInterfaceString = "{" +
                "\"hash\":\"anything\"," +
                "\"entrypoint\":\"Main\"," +
                "\"functions\":null," +
                "\"events\":null" +
                "}";

        BhpContractInterface bhpContractInterface = objectMapper.readValue(bhpContractInterfaceString, BhpContractInterface.class);

        assertThat(bhpContractInterface.getHash(), is("anything"));
        assertThat(bhpContractInterface.getEntryPoint(), is("Main"));
        assertThat(bhpContractInterface.getFunctions(), emptyCollectionOf(BhpContractFunction.class));
        assertThat(bhpContractInterface.getFunctions(), hasSize(0));
        assertThat(bhpContractInterface.getEvents(), emptyCollectionOf(BhpContractEvent.class));
        assertThat(bhpContractInterface.getEvents(), hasSize(0));
    }

}
