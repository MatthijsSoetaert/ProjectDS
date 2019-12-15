package Models.Generators;

import Models.Generators.KeyGenerator;
import Models.Objects.Session;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public final class EncryptionGenerator
{
    public static byte[] decryptMessage(SecretKey key, byte[] message) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
    {
        Cipher cipher1 = Cipher.getInstance("AES");
        cipher1.init(Cipher.DECRYPT_MODE,key);
        byte[] decryptedMessage = cipher1.doFinal(message);
        return decryptedMessage;
    }

    public static byte[] encryptMessage(Session session, byte[] messageBytes) throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException
    {
        SecretKey originalKey = KeyGenerator.recreateKey(session.getSendSymKey());
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,originalKey);
        byte[] finalString = cipher.doFinal(messageBytes);
        return finalString;
    }
}
