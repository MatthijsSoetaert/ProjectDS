package Models.Generators;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class KeyGenerator {

    public static SecretKey generateSymmetricKey(String type, int length) throws NoSuchAlgorithmException {
        javax.crypto.KeyGenerator symKeyGenerator = javax.crypto.KeyGenerator.getInstance(type);
        symKeyGenerator.init(length);
        return symKeyGenerator.generateKey();
    }

    public static String deriveKey(String prevKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory kf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(prevKey.toCharArray(), prevKey.getBytes(), 1024, 256);
        SecretKey tmp = kf.generateSecret(spec);
        String encodedKey = Base64.getEncoder().encodeToString(tmp.getEncoded());
        System.out.println("dit is de afgelijde sleutel: " + encodedKey);

        return encodedKey;
    }

    public static SecretKey recreateKey(String symKeyString){
        byte[] decodedKey = Base64.getDecoder().decode(symKeyString);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }
}
