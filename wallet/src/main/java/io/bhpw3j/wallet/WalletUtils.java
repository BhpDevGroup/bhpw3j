package io.bhpw3j.wallet;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.bhpw3j.crypto.Credentials;
import io.bhpw3j.crypto.ECKeyPair;
import io.bhpw3j.crypto.BRC2;
import io.bhpw3j.crypto.ScryptParams;
import io.bhpw3j.crypto.SecureRandomUtils;
import io.bhpw3j.crypto.exceptions.CipherException;
import io.bhpw3j.crypto.exceptions.BRC2AccountNotFound;
import io.bhpw3j.crypto.exceptions.BRC2InvalidFormat;
import io.bhpw3j.crypto.exceptions.BRC2InvalidPassphrase;
import io.bhpw3j.wallet.brc6.BRC6Account;
import io.bhpw3j.wallet.brc6.BRC6Wallet;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility functions for working with Wallet files.
 */
public class WalletUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final SecureRandom secureRandom = SecureRandomUtils.secureRandom();

    static {
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String generateNewWalletFile(
            String password, File destinationDirectory)
            throws CipherException, IOException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, NoSuchProviderException {

        ECKeyPair ecKeyPair = ECKeyPair.createEcKeyPair();
        return generateWalletFile(password, ecKeyPair, destinationDirectory);
    }

    public static String generateWalletFile(
            String password, ECKeyPair ecKeyPair, File destinationDirectory)
            throws CipherException, IOException {

        Account a = Account.fromECKeyPair(ecKeyPair).build();
        Wallet w = new Wallet.Builder().account(a).build();
        w.encryptAllAccounts(password);
        return generateWalletFile(w.toBRC6Wallet(), destinationDirectory);
    }

    public static String generateWalletFile(BRC6Wallet BRC6Wallet, File destinationDirectory)
            throws IOException {

        String fileName = getWalletFileName(BRC6Wallet);
        File destination = new File(destinationDirectory, fileName);

        objectMapper.writeValue(destination, BRC6Wallet);

        return fileName;
    }

    public static BRC6Wallet loadWalletFile(String source) throws IOException {
        return loadWalletFile(new File(source));
    }

    public static BRC6Wallet loadWalletFile(File source) throws IOException {
        return objectMapper.readValue(source, BRC6Wallet.class);
    }

    public static Credentials loadCredentials(String accountAddress, String password, String source)
            throws IOException, CipherException, BRC2InvalidFormat, BRC2InvalidPassphrase, BRC2AccountNotFound {
        return loadCredentials(accountAddress, password, new File(source));
    }

    public static Credentials loadCredentials(String accountAddress, String password, File source)
            throws IOException, CipherException, BRC2InvalidFormat, BRC2InvalidPassphrase, BRC2AccountNotFound {

        BRC6Wallet BRC6Wallet = objectMapper.readValue(source, BRC6Wallet.class);

        BRC6Account account = BRC6Wallet.getAccounts().stream()
                .filter((a) -> a.getAddress() != null)
                .filter((a) -> a.getAddress().equals(accountAddress))
                .findFirst()
                .orElseThrow(() -> new BRC2AccountNotFound("Account not found in the specified wallet."));

        return loadCredentials(account, password, BRC6Wallet);
    }

    public static Credentials loadCredentials(BRC6Account account, String password, BRC6Wallet BRC6Wallet)
            throws CipherException, BRC2InvalidFormat, BRC2InvalidPassphrase {
        ScryptParams scryptParams = BRC6Wallet.getScrypt();
        ECKeyPair decrypted = BRC2.decrypt(password, account.getKey(), scryptParams);
        return new Credentials(decrypted);
    }

    public static String getWalletFileName(BRC6Wallet BRC6Wallet) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(
                "'UTC--'yyyy-MM-dd'T'HH-mm-ss.nVV'--'");
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);

        return now.format(format) + BRC6Wallet.getName() + ".json";
    }

    public static String getDefaultKeyDirectory() {
        return getDefaultKeyDirectory(System.getProperty("os.name"));
    }

    static String getDefaultKeyDirectory(String osName) {
        String osNameLowerCase = osName.toLowerCase();

        if (osNameLowerCase.startsWith("mac")) {
            return String.format(
                    "%s%sLibrary%sbhpw3j", System.getProperty("user.home"), File.separator,
                    File.separator);
        } else if (osNameLowerCase.startsWith("win")) {
            return String.format("%s%sbhpw3j", System.getenv("APPDATA"), File.separator);
        } else {
            return String.format("%s%s.bhpw3j", System.getProperty("user.home"), File.separator);
        }
    }

}
