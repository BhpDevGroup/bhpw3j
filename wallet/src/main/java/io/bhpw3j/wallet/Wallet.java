package io.bhpw3j.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bhpw3j.crypto.BRC2;
import io.bhpw3j.crypto.ScryptParams;
import io.bhpw3j.crypto.exceptions.CipherException;
import io.bhpw3j.crypto.exceptions.BRC2InvalidFormat;
import io.bhpw3j.crypto.exceptions.BRC2InvalidPassphrase;
import io.bhpw3j.wallet.brc6.BRC6Account;
import io.bhpw3j.wallet.brc6.BRC6Wallet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.bhpw3j.crypto.SecurityProviderChecker.addBouncyCastle;

public class Wallet {

    private static final String DEFAULT_WALLET_NAME = "bhpw3jWallet";

    public static final String CURRENT_VERSION = "1.0";

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private String name;

    private String version;

    private List<Account> accounts = new ArrayList<>();

    private ScryptParams scryptParams;

    static {
        addBouncyCastle();
    }

    private Wallet() {
    }

    protected Wallet(Builder builder) {
        this.name = builder.name;
        this.version = builder.version;
        this.scryptParams = builder.scryptParams;
        this.accounts = builder.accounts;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * Sets the account at the given index to be the default account.
     * The previous default account is unset.
     *
     * @param index the index of the new default account.
     */
    public void setDefaultAccount(int index) {
        for (int i = 0; i < accounts.size(); i++) {
            accounts.get(i).setIsDefault(i == index);
        }
    }

    public ScryptParams getScryptParams() {
        return scryptParams;
    }

    public Account getDefaultAccount() {
        return this.accounts.stream().filter(Account::isDefault)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No default account found."));
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Adds the given account to this wallet.
     *
     * @param account The account to add.
     * @return true if the account was added, false if an account with that address was already in
     * the wallet.
     */
    public boolean addAccount(Account account) {
        if (accounts.stream().anyMatch(acc -> acc.getAddress().equals(account.getAddress()))) {
            return false;
        }
        accounts.add(account);
        return true;
    }

    /**
     * Removes the account with the given address from this wallet.
     *
     * @param address The address of the account to be removed.
     * @return true if an account was removed, false if no account with the given address was found.
     */
    public boolean removeAccount(String address) {
        return accounts.removeIf(acc -> acc.getAddress().equals(address));
    }

    public void decryptAllAccounts(String password)
            throws BRC2InvalidFormat, CipherException, BRC2InvalidPassphrase {

        for (Account acct : accounts) {
            acct.decryptPrivateKey(password, scryptParams);
        }
    }

    public void encryptAllAccounts(String password) throws CipherException {

        for (Account acct : accounts) {
            acct.encryptPrivateKey(password, scryptParams);
        }
    }

    public BRC6Wallet toBRC6Wallet() {
        List<BRC6Account> accts = accounts.stream().map(
                a -> a.toBRC6Account()).collect(Collectors.toList());
        return new BRC6Wallet(name, version, scryptParams, accts, null);
    }

    public static Builder fromBRC6Wallet(String brc6WalletFileName) throws IOException {
        return fromBRC6Wallet(Wallet.class.getClassLoader().getResourceAsStream(brc6WalletFileName));
    }

    public static Builder fromBRC6Wallet(URI brc6WalletFileUri) throws IOException {
        return fromBRC6Wallet(brc6WalletFileUri.toURL().openStream());
    }

    public static Builder fromBRC6Wallet(File brc6WalletFile) throws IOException {
        return fromBRC6Wallet(new FileInputStream(brc6WalletFile));
    }

    public static Builder fromBRC6Wallet(InputStream brc6WalletFileInputStream) throws IOException {
        BRC6Wallet BRC6Wallet = OBJECT_MAPPER.readValue(brc6WalletFileInputStream, BRC6Wallet.class);
        return fromBRC6Wallet(BRC6Wallet);
    }

    public static Builder fromBRC6Wallet(BRC6Wallet BRC6Wallet) {
        Builder b = new Builder();
        b.name = BRC6Wallet.getName();
        b.version = BRC6Wallet.getVersion();
        b.scryptParams = BRC6Wallet.getScrypt();
        for (BRC6Account brc6Acct : BRC6Wallet.getAccounts()) {
            b.accounts.add(Account.fromBRC6Account(brc6Acct).build());
        }
        return b;
    }

    /**
     * Creates a BRC6 compatible wallet file.
     *
     * @param destination the file that the wallet file should be saved.
     * @return the new wallet.
     * @throws IOException throws if failed to create the wallet on disk.
     */
    public Wallet saveBRC6Wallet(File destination) throws IOException {
        if (destination == null) {
            throw new IllegalArgumentException("Destination file cannot be null");
        }

        BRC6Wallet BRC6Wallet = toBRC6Wallet();

        if (!destination.isFile()) {
            throw new IllegalArgumentException("Destination file cannot be a directory");
        }

        OBJECT_MAPPER.writeValue(destination, BRC6Wallet);
        return this;
    }

    /**
     * Creates a new wallet with one account that is set as the default account.
     *
     * @return the new wallet.
     */
    public static Wallet createGenericWallet() {
        Account a = getNewDefaultAccount();
        return new Builder().account(a).build();
    }

    /**
     * Creates a new wallet with one account that is set as the default account.
     * Encrypts such account with the password.
     *
     * @param password password used to encrypt the account.
     * @return the new wallet.
     * @throws CipherException throws if failed encrypt the created wallet.
     */
    public static Wallet createGenericWallet(final String password)
            throws CipherException {
        Account a = getNewDefaultAccount();
        Wallet wallet = new Builder().account(a).build();
        wallet.encryptAllAccounts(password);
        return wallet;
    }

    /**
     * Creates a new wallet with one account that is set as the default account.
     * Also, encrypts such account and persists the BRC6 wallet to a file.
     *
     * @param password    password used to encrypt the account.
     * @param destination destination to the new BRC6 wallet file.
     * @return the new wallet.
     * @throws IOException     throws if failed to create the wallet on disk.
     * @throws CipherException throws if failed encrypt the created wallet.
     */
    public static Wallet createGenericWallet(String password, File destination)
            throws CipherException, IOException {
        Wallet wallet = createGenericWallet(password);
        wallet.saveBRC6Wallet(destination);
        return wallet;
    }

    private static Account getNewDefaultAccount() {
        return Account.fromNewECKeyPair().isDefault(true).build();
    }

    public static class Builder {

        String name;
        String version;
        List<Account> accounts;
        ScryptParams scryptParams;

        public Builder() {
            this.name = DEFAULT_WALLET_NAME;
            this.version = CURRENT_VERSION;
            this.accounts = new ArrayList<>();
            this.scryptParams = BRC2.DEFAULT_SCRYPT_PARAMS;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder accounts(List<Account> accounts) {
            this.accounts.addAll(accounts);
            return this;
        }

        public Builder account(Account account) {
            this.accounts.add(account);
            return this;
        }

        public Builder scryptParams(ScryptParams scryptParams) {
            this.scryptParams = scryptParams;
            return this;
        }

        public Wallet build() {
            return new Wallet(this);
        }
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", accounts=" + accounts +
                ", scryptParams=" + scryptParams +
                '}';
    }
}
