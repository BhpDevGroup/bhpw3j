package io.bhpw3j.contract.abi;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.bhpw3j.contract.abi.exceptions.BRC3Exception;
import io.bhpw3j.contract.abi.exceptions.BRC3ParsingException;
import io.bhpw3j.contract.abi.model.BhpContractInterface;
import io.bhpw3j.utils.Numeric;
import io.bhpw3j.utils.Strings;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class BhpABIUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

    static {
        prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
        objectMapper.setDefaultPrettyPrinter(prettyPrinter);
    }

    public static BhpContractInterface loadABIFile(String absoluteFileName) throws BRC3Exception {
        return loadABIFile(new File(absoluteFileName));
    }

    public static BhpContractInterface loadABIFile(File source) throws BRC3Exception {
        try {
            return objectMapper.readValue(source, BhpContractInterface.class);
        } catch (Exception e) {
            throw new BRC3ParsingException("Could not load the ABI file in the parsing process.", e);
        }
    }

    public static BhpContractInterface loadABIFile(InputStream source) throws BRC3Exception {
        try {
            return objectMapper.readValue(source, BhpContractInterface.class);
        } catch (Exception e) {
            throw new BRC3ParsingException("Could not load the ABI file in the parsing process.", e);
        }
    }

    public static String generateBhpContractInterface(BhpContractInterface bhpContractInfo, File destinationDirectory)
            throws IOException {

        String fileName = getABIFileName(bhpContractInfo);
        File destination = new File(destinationDirectory, fileName);

        objectMapper.writeValue(destination, bhpContractInfo);

        return fileName;
    }

    private static String getABIFileName(BhpContractInterface bhpContractInfo) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(
                "'UTC--'yyyy-MM-dd'T'HH-mm-ss.nVV'--'");
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);

        String abiName = "bhpw3j";
        if (Strings.isEmpty(bhpContractInfo.getHash())) {
            abiName = Numeric.cleanHexPrefix(bhpContractInfo.getHash());
        }
        return now.format(format) + abiName + ".abi";
    }

}
