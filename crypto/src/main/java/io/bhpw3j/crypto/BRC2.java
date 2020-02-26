package io.bhpw3j.crypto;

import io.bhpw3j.crypto.exceptions.CipherException;
import io.bhpw3j.crypto.exceptions.BRC2InvalidFormat;
import io.bhpw3j.crypto.exceptions.BRC2InvalidPassphrase;
import io.bhpw3j.utils.Numeric;
import org.bouncycastle.crypto.generators.SCrypt;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;

import static io.bhpw3j.constants.BHPConstants.PRIVATE_KEY_SIZE;
import static io.bhpw3j.crypto.Hash.sha256;
import static io.bhpw3j.utils.ArrayUtils.concatenate;
import static io.bhpw3j.utils.ArrayUtils.getFirstNBytes;
import static io.bhpw3j.utils.ArrayUtils.getLastNBytes;
import static io.bhpw3j.utils.ArrayUtils.xor;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Provides encryption and decryption functionality according to BRC-2 specification.
 */
public class BRC2 {

    public static final int DKLEN = 64;
    public static final int BRC2_PRIVATE_KEY_LENGTH = 39;
    public static final byte BRC2_PREFIX_1 = (byte) 0x01;
    public static final byte BRC2_PREFIX_2 = (byte) 0x42;
    public static final byte BRC2_FLAGBYTE = (byte) 0xE0;
    public static final int N_STANDARD = 1 << 14;
    public static final int P_STANDARD = 8;
    public static final int R_STANDARD = 8;
    public static final ScryptParams DEFAULT_SCRYPT_PARAMS =
            new ScryptParams(N_STANDARD, P_STANDARD, R_STANDARD);

    /**
     * Decrypts the given encrypted private key in BRC-2 format with the given password and standard
     * scrypt parameters.
     *
     * @param password   The passphrase used for decryption.
     * @param brc2String The BRC-2 ecnrypted private key.
     * @return an EC key pair constructed form the decrypted private key.
     * @throws BRC2InvalidFormat     throws if the encrypted BRC2 has an invalid format.
     * @throws CipherException       throws if failed encrypt the created wallet.
     * @throws BRC2InvalidPassphrase throws if the passphrase is not valid.
     */
    public static ECKeyPair decrypt(String password, String brc2String)
            throws CipherException, BRC2InvalidFormat, BRC2InvalidPassphrase {
        return decrypt(password, brc2String, new ScryptParams(N_STANDARD, P_STANDARD, R_STANDARD));
    }

    /**
     * Decrypts the given encrypted private key in BRC-2 format with the given password and scrypt
     * parameters.
     *
     * @param password     The passphrase used for decryption.
     * @param brc2String   The BRC-2 ecnrypted private key.
     * @param scryptParams The scrypt parameters used for encryption.
     * @return an EC key pair constructed form the decrypted private key.
     * @throws BRC2InvalidFormat     throws if the encrypted BRC2 has an invalid format.
     * @throws CipherException       throws if failed encrypt the created wallet.
     * @throws BRC2InvalidPassphrase throws if the passphrase is not valid.
     */
    public static ECKeyPair decrypt(String password, String brc2String, ScryptParams scryptParams)
            throws BRC2InvalidFormat, CipherException, BRC2InvalidPassphrase {

        byte[] brc2Data = Base58.base58CheckDecode(brc2String);

        if (brc2Data.length != BRC2_PRIVATE_KEY_LENGTH || brc2Data[0] != BRC2_PREFIX_1 || brc2Data[1] != BRC2_PREFIX_2 || brc2Data[2] != BRC2_FLAGBYTE) {
            throw new BRC2InvalidFormat("Not valid BRC2 prefix.");
        }

        byte[] addressHash = new byte[4];
        // copy 4 bytes related to the address hash
        System.arraycopy(brc2Data, 3, addressHash, 0, 4);

        byte[] derivedKey = generateDerivedScryptKey(
                password.getBytes(UTF_8), addressHash, scryptParams, DKLEN);

        byte[] derivedKeyHalf1 = getFirstNBytes(derivedKey, 32);
        byte[] derivedKeyHalf2 = getLastNBytes(derivedKey, 32);

        byte[] encrypted = new byte[32];
        System.arraycopy(brc2Data, 7, encrypted, 0, 32);

        byte[] decrypted = performCipherOperation(Cipher.DECRYPT_MODE, encrypted, derivedKeyHalf2);

        byte[] plainPrivateKey = xor(decrypted, derivedKeyHalf1);

        ECKeyPair ecKeyPair = ECKeyPair.create(plainPrivateKey);
        byte[] calculatedAddressHash = getAddressHash(ecKeyPair);

        if (!Arrays.equals(calculatedAddressHash, addressHash)) {
            throw new BRC2InvalidPassphrase("Calculated address hash does not match the one in the provided encrypted address.");
        }

        return ecKeyPair;
    }

    /**
     * Encrypts the private key of the given key pair with the given password using standard Scrypt
     * parameters.
     *
     * @param password  The passphrase used for encryption.
     * @param ecKeyPair the {@link ECKeyPair} to be encrypted
     * @return The BRC-2 encrypted password.
     * @throws CipherException throws if the key pair cannot be encrypted.
     */
    public static String encrypt(String password, ECKeyPair ecKeyPair) throws CipherException {
        return encrypt(password, ecKeyPair, N_STANDARD, P_STANDARD, R_STANDARD);
    }

    /**
     * Encrypts the private key of the given EC key pair following the BRC-2 standard.
     *
     * @param password     the passphrase to be used to encrypt
     * @param ecKeyPair    the {@link ECKeyPair} to be encrypted
     * @param scryptParams the scrypt parameters used for encryption.
     * @return encrypted private key as described on BRC-2.
     * @throws CipherException thrown when the AES/ECB/NoPadding cipher operation fails
     */
    public static String encrypt(String password, ECKeyPair ecKeyPair, ScryptParams scryptParams)
            throws CipherException {

        return encrypt(password, ecKeyPair, scryptParams.getN(), scryptParams.getP(), scryptParams.getR());
    }

    /**
     * Encrypts the private key of the given EC key pair following the BRC-2 standard.
     *
     * @param password  the passphrase to be used to encrypt
     * @param ecKeyPair the {@link ECKeyPair} to be encrypted
     * @param n         the "n" parameter for {@link SCrypt#generate(byte[], byte[], int, int, int, int)} method
     * @param p         the "p" parameter for {@link SCrypt#generate(byte[], byte[], int, int, int, int)} method
     * @param r         the "r" parameter for {@link SCrypt#generate(byte[], byte[], int, int, int, int)} method
     * @return encrypted private key as described on BRC-2.
     * @throws CipherException thrown when the AES/ECB/NoPadding cipher operation fails
     */
    public static String encrypt(String password, ECKeyPair ecKeyPair, int n, int p, int r)
            throws CipherException {

        byte[] addressHash = getAddressHash(ecKeyPair);

        byte[] derivedKey = generateDerivedScryptKey(
                password.getBytes(UTF_8), addressHash, n, r, p, DKLEN);

        byte[] derivedHalf1 = getFirstNBytes(derivedKey, 32);
        byte[] derivedHalf2 = getLastNBytes(derivedKey, 32);

        byte[] encryptedHalf1 = performCipherOperation(
                Cipher.ENCRYPT_MODE,
                xorPrivateKeyAndDerivedHalf(ecKeyPair, derivedHalf1, 0, 16),
                derivedHalf2);

        byte[] encryptedHalf2 = performCipherOperation(
                Cipher.ENCRYPT_MODE,
                xorPrivateKeyAndDerivedHalf(ecKeyPair, derivedHalf1, 16, 32),
                derivedHalf2);

        byte[] prefixes = new byte[3];
        // prefix
        prefixes[0] = BRC2_PREFIX_1;
        prefixes[1] = BRC2_PREFIX_2;
        // flagbyte, which is always the same
        prefixes[2] = BRC2_FLAGBYTE;

        byte[] concatenation = concatenate(prefixes, addressHash, encryptedHalf1, encryptedHalf2);
        return Base58.base58CheckEncode(concatenation);
    }

    private static byte[] xorPrivateKeyAndDerivedHalf(ECKeyPair ecKeyPair, byte[] derivedHalf, int from, int to) {
        return xor(
                Arrays.copyOfRange(privateKeyToBytes(ecKeyPair), from, to),
                Arrays.copyOfRange(derivedHalf, from, to)
        );
    }

    private static byte[] privateKeyToBytes(ECKeyPair ecKeyPair) {
        return Numeric.toBytesPadded(ecKeyPair.getPrivateKey(), PRIVATE_KEY_SIZE);
    }

    private static byte[] generateDerivedScryptKey(
            byte[] password, byte[] salt, int n, int r, int p, int dkLen) {
        return SCrypt.generate(password, salt, n, r, p, dkLen);
    }

    private static byte[] generateDerivedScryptKey(
            byte[] password, byte[] salt, ScryptParams scryptParams, int dkLen) {
        return SCrypt.generate(password, salt, scryptParams.getN(), scryptParams.getR(), scryptParams.getP(), dkLen);
    }

    public static byte[] performCipherOperation(
            int mode, byte[] data, byte[] encryptKey) throws CipherException {

        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding", BouncyCastleProvider.PROVIDER_NAME);

            SecretKeySpec secretKeySpec = new SecretKeySpec(encryptKey, "AES");
            cipher.init(mode, secretKeySpec);
            return cipher.doFinal(data);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | NoSuchProviderException
                | BadPaddingException | IllegalBlockSizeException e) {
            throw new CipherException("Error performing cipher operation", e);
        }
    }

    public static byte[] getAddressHash(ECKeyPair ecKeyPair) {
        String address = ecKeyPair.getAddress();
        byte[] addressHashed = sha256(sha256(address.getBytes()));
        return getFirstNBytes(addressHashed, 4);
    }
}
