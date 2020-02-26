package io.bhpw3j.contract.abi.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.bhpw3j.contract.ContractParameter;
import io.bhpw3j.model.types.ContractParameterType;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

public class BhpContractFunctionTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testSerialize() throws JsonProcessingException {
        BhpContractFunction bhpContractFunction = new BhpContractFunction(
                "anything",
                Arrays.asList(
                        ContractParameter.byteArray("001010101010")
                ),
                ContractParameterType.BYTE_ARRAY
        );
        String bhpContractFunctionString = objectMapper.writeValueAsString(bhpContractFunction);

        assertThat(bhpContractFunctionString,
                is(
                        "{" +
                                "\"name\":\"anything\"," +
                                "\"parameters\":[" +
                                "{" +
                                "\"type\":\"ByteArray\"," +
                                "\"value\":\"001010101010\"" +
                                "}" +
                                "]," +
                                "\"returntype\":\"ByteArray\"" +
                                "}"
                )
        );
    }

    @Test
    public void testSerialize_Empty() throws JsonProcessingException {
        BhpContractFunction bhpContractFunction = new BhpContractFunction(
                "anything",
                Arrays.asList(),
                ContractParameterType.BYTE_ARRAY
        );
        String bhpContractFunctionString = objectMapper.writeValueAsString(bhpContractFunction);

        assertThat(bhpContractFunctionString,
                is(
                        "{" +
                                "\"name\":\"anything\"," +
                                "\"parameters\":[" +
                                "]," +
                                "\"returntype\":\"ByteArray\"" +
                                "}"
                )
        );
    }

    @Test
    public void testSerialize_Null() throws JsonProcessingException {
        BhpContractFunction bhpContractFunction = new BhpContractFunction(
                "anything",
                null,
                ContractParameterType.BYTE_ARRAY
        );
        String bhpContractFunctionString = objectMapper.writeValueAsString(bhpContractFunction);

        assertThat(bhpContractFunctionString,
                is(
                        "{" +
                                "\"name\":\"anything\"," +
                                "\"parameters\":[" +
                                "]," +
                                "\"returntype\":\"ByteArray\"" +
                                "}"
                )
        );
    }

    @Test
    public void testDeserialize() throws IOException {

        String bhpContractFunctionString = "{" +
                "\"name\":\"anything\"," +
                "\"parameters\":[" +
                "{" +
                "\"type\":\"ByteArray\"," +
                "\"value\":\"001010101010\"" +
                "}" +
                "]," +
                "\"returntype\":\"ByteArray\"" +
                "}";

        BhpContractFunction bhpContractFunction = objectMapper.readValue(bhpContractFunctionString, BhpContractFunction.class);

        assertThat(bhpContractFunction.getName(), is("anything"));
        assertThat(bhpContractFunction.getParameters(), not(emptyCollectionOf(ContractParameter.class)));
        assertThat(bhpContractFunction.getParameters(),
                hasItems(
                        ContractParameter.byteArray("001010101010")
                )
        );
        assertThat(bhpContractFunction.getReturnType(), is(ContractParameterType.BYTE_ARRAY));
    }

    @Test
    public void testDeserialize_Empty() throws IOException {

        String bhpContractFunctionString = "{" +
                "\"name\":\"anything\"," +
                "\"parameters\":[" +
                "]," +
                "\"returntype\":\"ByteArray\"" +
                "}";

        BhpContractFunction bhpContractFunction = objectMapper.readValue(bhpContractFunctionString, BhpContractFunction.class);

        assertThat(bhpContractFunction.getName(), is("anything"));
        assertThat(bhpContractFunction.getParameters(), emptyCollectionOf(ContractParameter.class));
        assertThat(bhpContractFunction.getParameters(), hasSize(0));
        assertThat(bhpContractFunction.getReturnType(), is(ContractParameterType.BYTE_ARRAY));
    }

    @Test
    public void testDeserialize_Null() throws IOException {

        String bhpContractFunctionString = "{" +
                "\"name\":\"anything\"," +
                "\"parameters\":null," +
                "\"returntype\":\"ByteArray\"" +
                "}";

        BhpContractFunction bhpContractFunction = objectMapper.readValue(bhpContractFunctionString, BhpContractFunction.class);

        assertThat(bhpContractFunction.getName(), is("anything"));
        assertThat(bhpContractFunction.getParameters(), emptyCollectionOf(ContractParameter.class));
        assertThat(bhpContractFunction.getParameters(), hasSize(0));
        assertThat(bhpContractFunction.getReturnType(), is(ContractParameterType.BYTE_ARRAY));
    }

}
