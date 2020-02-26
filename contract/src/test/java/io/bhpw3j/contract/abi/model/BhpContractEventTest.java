package io.bhpw3j.contract.abi.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.bhpw3j.contract.ContractParameter;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class BhpContractEventTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testSerialize() throws JsonProcessingException {
        BhpContractEvent bhpContractEvent = new BhpContractEvent(
                "anything",
                Arrays.asList(
                        ContractParameter.byteArray("001010101010"),
                        ContractParameter.bool(true),
                        ContractParameter.integer(123)
                )
        );
        String bhpContractEventString = objectMapper.writeValueAsString(bhpContractEvent);

        assertThat(bhpContractEventString,
                is(
                        "{" +
                                "\"name\":\"anything\"," +
                                "\"parameters\":[" +
                                "{" +
                                "\"type\":\"ByteArray\"," +
                                "\"value\":\"001010101010\"" +
                                "}," +
                                "{" +
                                "\"type\":\"Boolean\"," +
                                "\"value\":true" +
                                "}," +
                                "{" +
                                "\"type\":\"Integer\"," +
                                "\"value\":\"123\"" +
                                "}" +
                                "]" +
                                "}"
                )
        );
    }

    @Test
    public void testSerialize_Empty() throws JsonProcessingException {
        BhpContractEvent bhpContractEvent = new BhpContractEvent(
                "anything",
                Arrays.asList()
        );
        String bhpContractEventString = objectMapper.writeValueAsString(bhpContractEvent);

        assertThat(bhpContractEventString,
                is(
                        "{" +
                                "\"name\":\"anything\"," +
                                "\"parameters\":[" +
                                "]" +
                                "}"
                )
        );
    }

    @Test
    public void testSerialize_Null() throws JsonProcessingException {
        BhpContractEvent bhpContractEvent = new BhpContractEvent(
                "anything",
                null
        );
        String bhpContractEventString = objectMapper.writeValueAsString(bhpContractEvent);

        assertThat(bhpContractEventString,
                is(
                        "{" +
                                "\"name\":\"anything\"," +
                                "\"parameters\":[" +
                                "]" +
                                "}"
                )
        );
    }

    @Test
    public void testDeserialize() throws IOException {

        String bhpContractEventString = "{" +
                "\"name\":\"anything\"," +
                "\"parameters\":[" +
                "{" +
                "\"type\":\"ByteArray\"," +
                "\"value\":\"001010101010\"" +
                "}," +
                "{" +
                "\"type\":\"Boolean\"," +
                "\"value\":true" +
                "}," +
                "{" +
                "\"type\":\"Integer\"," +
                "\"value\":\"123\"" +
                "}" +
                "]" +
                "}";

        BhpContractEvent bhpContractEvent = objectMapper.readValue(bhpContractEventString, BhpContractEvent.class);

        assertThat(bhpContractEvent.getName(), is("anything"));
        assertThat(bhpContractEvent.getParameters(), not(emptyCollectionOf(ContractParameter.class)));
        assertThat(bhpContractEvent.getParameters(),
                hasItems(
                        ContractParameter.byteArray("001010101010"),
                        ContractParameter.bool(true),
                        ContractParameter.integer(123)
                )
        );
    }

    @Test
    public void testDeserialize_Empty() throws IOException {

        String bhoContractEventString = "{" +
                "\"name\":\"anything\"," +
                "\"parameters\":[" +
                "]" +
                "}";

        BhpContractEvent bhpContractEvent = objectMapper.readValue(bhoContractEventString, BhpContractEvent.class);

        assertThat(bhpContractEvent.getName(), is("anything"));
        assertThat(bhpContractEvent.getParameters(), emptyCollectionOf(ContractParameter.class));
        assertThat(bhpContractEvent.getParameters(), hasSize(0));
    }

    @Test
    public void testDeserialize_Null() throws IOException {

        String bhpContractEventString = "{" +
                "\"name\":\"anything\"," +
                "\"parameters\":null" +
                "}";

        BhpContractEvent bhpContractEvent = objectMapper.readValue(bhpContractEventString, BhpContractEvent.class);

        assertThat(bhpContractEvent.getName(), is("anything"));
        assertThat(bhpContractEvent.getParameters(), emptyCollectionOf(ContractParameter.class));
        assertThat(bhpContractEvent.getParameters(), hasSize(0));
    }

}
