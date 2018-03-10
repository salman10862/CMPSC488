package pennychain.db;

import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import javax.xml.bind.DatatypeConverter;

public class Hash {

    private static final int SALT_BYTES = 32;
    private static final int HASH_BYTES = 32;
    private static final int ITERATIONS = 1000;
    private static final String PBKDF2_ALG = "PBKDF2WithHmacSHA1";

    /* getHashAndSalt : CharSequence -> String
     * returns salt and salted+hashed password
     * as a String in the format salt:saltedHashedPw
     */

    public static String getHashAndSalt(CharSequence password)
        throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTES];
        random.nextBytes(salt);

        PBEKeySpec pbeKeySpec = new PBEKeySpec(
                password.toString().toCharArray(),
                salt,
                ITERATIONS,
                HASH_BYTES * 8); // key length is measured in bits

        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALG);
        byte[] hashedPass = skf.generateSecret(pbeKeySpec).getEncoded();

        String result = DatatypeConverter.printBase64Binary(salt) + ":" +
                        DatatypeConverter.printBase64Binary(hashedPass);

        return result;
    }

    public static boolean verifyPassword(CharSequence password,
            String b64EncodedSalt, String b64EncodedSaltedHash)
        throws NoSuchAlgorithmException, InvalidKeySpecException {
        
        byte[] salt = 
            DatatypeConverter.parseBase64Binary(b64EncodedSalt);
        byte[] hash = 
            DatatypeConverter.parseBase64Binary(b64EncodedSaltedHash);

        PBEKeySpec pbeKeySpec = new PBEKeySpec(
                password.toString().toCharArray(),
                salt,
                ITERATIONS,
                HASH_BYTES * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALG);

        byte[] hashedTestPw = skf.generateSecret(pbeKeySpec).getEncoded();
        String b64HashedTestPw = 
                DatatypeConverter.printBase64Binary(hashedTestPw);

        return b64HashedTestPw.equals(b64EncodedSaltedHash);
    }
}

