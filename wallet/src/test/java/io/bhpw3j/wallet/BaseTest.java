package io.bhpw3j.wallet;

import io.bhpw3j.crypto.Credentials;
import io.bhpw3j.wallet.brc6.BRC6Wallet;
import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.nio.file.Files;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class BaseTest {

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

    public File getTempDir() {
        return tempDir;
    }

    protected void testGeneratedNewWalletFile(BRC6Wallet BRC6Wallet) throws Exception {
        WalletUtils.loadCredentials(BRC6Wallet.getAccounts().stream().findFirst().get(), SampleKeys.PASSWORD_1, BRC6Wallet);
    }

    protected void testGenerateWalletFile(String fileName) throws Exception {
        Credentials credentials = WalletUtils.loadCredentials(SampleKeys.ADDRESS_1,
                SampleKeys.PASSWORD_1, new File(tempDir, fileName));

        assertThat(credentials, equalTo(SampleKeys.CREDENTIALS_1));
    }

    protected static File createTempDir() throws Exception {
        return Files.createTempDirectory(
                WalletUtilsTest.class.getSimpleName() + "-testkeys").toFile();
    }

}
