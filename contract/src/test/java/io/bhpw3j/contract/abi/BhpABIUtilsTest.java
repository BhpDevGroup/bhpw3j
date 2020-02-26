package io.bhpw3j.contract.abi;

import io.bhpw3j.contract.abi.exceptions.BRC3ParsingException;
import io.bhpw3j.contract.abi.model.BhpContractEvent;
import io.bhpw3j.contract.abi.model.BhpContractFunction;
import io.bhpw3j.contract.abi.model.BhpContractInterface;
import java.util.Arrays;
import org.bouncycastle.util.Strings;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class BhpABIUtilsTest {

    private static final String TEST1_SMARTCONTRACT_ABI_FILENAME = "/test1-smartcontract.abi.json";
    private static final String TEST2_SMARTCONTRACT_ABI_FILENAME = "/test2-smartcontract.abi.json";
    private static final String TEST3_SMARTCONTRACT_ABI_FILENAME = "/test3-smartcontract.abi.json";
    private static final String TEST4_SMARTCONTRACT_ABI_FILENAME = "/test4-smartcontract.abi.json";
    private static final String TEST5_SMARTCONTRACT_ABI_FILENAME = "/test5-smartcontract.abi.json";

    private File tempDir;

    @Before
    public void setUp() throws Exception {
        tempDir = createTempDir();
    }

    @After
    public void tearDown() {
        for (File file : tempDir.listFiles()) {
            file.delete();
        }
        tempDir.delete();
    }

    @Test
    public void testLoadCredentialsFromFile_SmartContract1() throws Exception {
        BhpContractInterface bhpContractABI = BhpABIUtils.loadABIFile(
                BhpABIUtilsTest.class.getResource(
                        TEST1_SMARTCONTRACT_ABI_FILENAME
                ).getFile());

        assertThat(bhpContractABI, notNullValue());
        assertThat(bhpContractABI.getHash(), is("0x5944fc67643207920ec129d13181297fed10350c"));
        assertThat(bhpContractABI.getEntryPoint(), is("Main"));
        assertThat(bhpContractABI.getFunctions(), hasSize(3));
        assertThat(bhpContractABI.getEvents(), emptyCollectionOf(BhpContractEvent.class));
    }

    @Test
    public void testLoadCredentialsFromFile_SmartContract2() throws Exception {
        BhpContractInterface bhpContractABI = BhpABIUtils.loadABIFile(
                BhpABIUtilsTest.class.getResource(
                        TEST2_SMARTCONTRACT_ABI_FILENAME
                ).getFile());

        assertThat(bhpContractABI, notNullValue());
        assertThat(bhpContractABI.getHash(), is("0x5944fc67643207920ec129d13181297fed10350c"));
        assertThat(bhpContractABI.getEntryPoint(), is("Main"));
        assertThat(bhpContractABI.getFunctions(), hasSize(3));
        assertThat(bhpContractABI.getEvents(), hasSize(2));
    }

    @Test(expected = BRC3ParsingException.class)
    public void testLoadCredentialsFromFile_SmartContract3() throws Exception {
        BhpABIUtils.loadABIFile(
                BhpABIUtilsTest.class.getResource(
                        TEST3_SMARTCONTRACT_ABI_FILENAME
                ).getFile());
    }

    @Test
    public void testLoadCredentialsFromFile_SmartContract4() throws Exception {
        BhpContractInterface bhpContractABI = BhpABIUtils.loadABIFile(
                BhpABIUtilsTest.class.getResource(
                        TEST4_SMARTCONTRACT_ABI_FILENAME
                ).getFile());

        assertThat(bhpContractABI, notNullValue());
        assertThat(bhpContractABI.getHash(), is("0x5944fc67643207920ec129d13181297fed10350c"));
        assertThat(bhpContractABI.getEntryPoint(), nullValue());
    }

    @Test
    public void testLoadCredentialsFromStream_SmartContract4() throws Exception {
        BhpContractInterface bhpContractABI = BhpABIUtils.loadABIFile(
                BhpABIUtilsTest.class.getResourceAsStream(
                        TEST4_SMARTCONTRACT_ABI_FILENAME)
        );

        assertThat(bhpContractABI, notNullValue());
        assertThat(bhpContractABI.getHash(), is("0x5944fc67643207920ec129d13181297fed10350c"));
        assertThat(bhpContractABI.getEntryPoint(), nullValue());
    }

    @Test
    public void testLoadCredentialsFromStream_SmartContract5() throws Exception {
        BhpContractInterface bhpContractABI = BhpABIUtils.loadABIFile(
            BhpABIUtilsTest.class.getResourceAsStream(
                TEST5_SMARTCONTRACT_ABI_FILENAME)
        );

        assertThat(bhpContractABI, notNullValue());
        assertThat(bhpContractABI.getHash(), is("0x5944fc67643207920ec129d13181297fed10350c"));
        assertThat(bhpContractABI.getEntryPoint(), is("Main"));
        assertThat(
            bhpContractABI.getFunctions().stream()
                .filter(f -> f.getName().equals(bhpContractABI.getEntryPoint()))
                .findFirst()
                .get(),
            is(new BhpContractFunction("Main", Arrays.asList(), null)));
    }

    @Test
    public void testGenerateBhpContractABIFile() throws Exception {
        // load test smartcontract ABI 2
        BhpContractInterface bhpContractABI = BhpABIUtils.loadABIFile(
                BhpABIUtilsTest.class.getResource(
                        TEST2_SMARTCONTRACT_ABI_FILENAME
                ).getFile());
        // generate the file based on the BhpContractInterface class
        String fileName = BhpABIUtils.generateBhpContractInterface(bhpContractABI, tempDir);

        assertThat(getResultFileContent(fileName), is(expectedTest2ABI()));
        assertThat(getResultFileContent(fileName), is(getTestFileContent(TEST2_SMARTCONTRACT_ABI_FILENAME)));
    }

    private static File createTempDir() throws Exception {
        File file = Files.createTempDirectory(
                BhpABIUtilsTest.class.getSimpleName() + "-abifiles").toFile();
        file.deleteOnExit();
        return file;
    }

    private String getTestFileContent(String testFile) throws IOException, URISyntaxException {
        return trimWhiteSpaces(
                Strings.fromByteArray(
                        Files.readAllBytes(Paths.get(BhpABIUtilsTest.class.getResource(testFile).toURI()))));
    }

    private String getResultFileContent(String fileName) throws IOException {
        return trimWhiteSpaces(
                Strings.fromByteArray(
                        Files.readAllBytes(Paths.get(tempDir.getAbsolutePath(), fileName))));
    }

    private String trimWhiteSpaces(String s) {
        return s.trim()
                .replaceAll("\n", "")
                .replaceAll("\\s+", "");
    }

    private File getResultFile(String fileName) {
        return Paths.get(tempDir.getAbsolutePath(), fileName).toFile();
    }

    private String expectedTest2ABI() {
        return trimWhiteSpaces("{\n" +
                "  \"hash\" : \"0x5944fc67643207920ec129d13181297fed10350c\",\n" +
                "  \"entrypoint\" : \"Main\",\n" +
                "  \"functions\" : [\n" +
                "    {\n" +
                "      \"name\" : \"Name\",\n" +
                "      \"parameters\" : [\n" +
                "        {\n" +
                "          \"name\" : \"nameParam1\",\n" +
                "          \"type\" : \"Integer\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"returntype\" : \"Array\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\" : \"Description\",\n" +
                "      \"parameters\" : [\n" +
                "        {\n" +
                "          \"name\" : \"descriptionParam1\",\n" +
                "          \"type\" : \"String\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"returntype\" : \"String\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\" : \"Main\",\n" +
                "      \"parameters\" : [\n" +
                "        {\n" +
                "          \"name\" : \"operation\",\n" +
                "          \"type\" : \"String\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\" : \"args\",\n" +
                "          \"type\" : \"Array\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"returntype\" : \"ByteArray\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"events\" : [\n" +
                "    {\n" +
                "      \"name\" : \"event1\",\n" +
                "      \"parameters\" : [\n" +
                "        {\n" +
                "          \"name\" : \"event1Param1\",\n" +
                "          \"type\" : \"String\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\" : \"event1Param2\",\n" +
                "          \"type\" : \"Array\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\" : \"event2\",\n" +
                "      \"parameters\" : [\n" +
                "        {\n" +
                "          \"name\" : \"event2Param1\",\n" +
                "          \"type\" : \"Integer\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\" : \"event2Param2\",\n" +
                "          \"type\" : \"Array\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}");
    }

}
