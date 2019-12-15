package MainPackage;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashGenerator {

    private static byte[] messageHash;

    public static byte[] generateHash(String message) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        messageHash = digest.digest(message.getBytes()); //HM
        return messageHash;
    }

    public static byte[] generateRandomString() throws NoSuchAlgorithmException{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 256; i++) {
            int j = 97 + (int) (Math.random() * 25);
            sb.append((char) j);
        }

        messageHash = digest.digest(sb.toString().getBytes());
        System.out.println("hashed message size: " + messageHash.length);//HM
        return messageHash;

    }
}
