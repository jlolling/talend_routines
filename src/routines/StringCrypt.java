package routines;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * Helper class for symmetric en/de-cryption of strings
 * @author lolling.jan
 */
public class StringCrypt {

    private static final String HASH_ALG  = "SHA1";
    private static final String ENC_ALG   = "TripleDES";

    /**
     * encrypt given user data
     * @param data userdata
     * @param passPhrase password for encryption and decryption
     * @return crypted data
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public static byte[] encrypt(final byte[] data, final String passPhrase) throws IOException, GeneralSecurityException {
        // Cipher-Objekt initialisieren
        final Cipher cipher = initCipher(true, passPhrase);
        // Daten aus der Quelldatei
        // in den Cipher-Stream schreiben
        final byte[] encryptedText = cipher.doFinal(data);
        return encryptedText;
    }

    /**
     * decrypt encrypted data into user data
     * @param cryptedData
     * @param passPhrase
     * @return user data
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public static byte[] decrypt(byte[] cryptedData, String passPhrase) throws IOException, GeneralSecurityException {
        final Cipher cipher = initCipher(false, passPhrase);
        // Daten aus der Quelldatei
        final byte[] decryptedData = cipher.doFinal(cryptedData);
        return decryptedData;
    }

    /**
     * 
     * @param encryptMode true=encrypt, false=decrypt
     * @param passPhrase needed for key generation
     * @return Cipher to use in cryting methods
     * @throws IOException
     * @throws GeneralSecurityException
     */
    private static final Cipher initCipher(final boolean encryptMode, final String passPhrase) throws IOException, GeneralSecurityException {
        // Passwort-Hash berechnen und Schl�ssel aufbereiten
        final byte[] rawKey = initSymmetricKey(passPhrase);
        // Initialisierung des TripleDES-Schl�ssels
        final Key key = new SecretKeySpec(new DESedeKeySpec(rawKey).getKey(), ENC_ALG);
        // Initialisierung des Cipher-Objekts   
        final Cipher cipher = Cipher.getInstance(ENC_ALG);
        if (encryptMode) {
            cipher.init(Cipher.ENCRYPT_MODE, key);
        } else {
            cipher.init(Cipher.DECRYPT_MODE, key);
        }
        return cipher;
    }

    /**
     * generates symmetric key
     * @param passPhrase
     * @return key
     * @throws IOException
     * @throws GeneralSecurityException
     */
    private static byte[] initSymmetricKey(String passPhrase) throws IOException, GeneralSecurityException {
        final MessageDigest md = MessageDigest.getInstance(HASH_ALG);
        final byte[] digest = md.digest(passPhrase.getBytes());
        final byte[] rawKey = new byte[24];
        System.arraycopy(digest, 0, rawKey, 0, 8);
        System.arraycopy(digest, 8, rawKey, 8, 8);
        // Erster und dritter Schlüssel sind bei TripleDES identisch
        System.arraycopy(digest, 0, rawKey, 16, 8);
        return rawKey;
    }
    
    /**
     * single step from raw data string to crypted base64 string
     * @param clearData
     * @param passPhrase
     * @return crypted base64 string
     * @throws Exception (also if passPhrase wrong)
     */
    public static String cryptToBcase64(String clearData, String passPhrase) throws Exception {
        final byte[] encryptedData = encrypt(clearData.getBytes(), passPhrase);
        return Base64.getEncoder().encodeToString(encryptedData);
    }
    
    /**
     * single step from crypted base64 coded string to raw data string
     * @param cryptedBase64 crypted base64 data
     * @param passPhrase
     * @return raw data string
     * @throws Exception (also if passPhrase wrong)
     */
    public static String decryptFromBase64(String cryptedBase64, String passPhrase) throws Exception {
        byte[] receivedCryptedData = Base64.getDecoder().decode(cryptedBase64);
        return new String(decrypt(receivedCryptedData, passPhrase));
    }

}