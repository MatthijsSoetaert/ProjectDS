package Models.Generators;

import Models.Generators.EncryptionGenerator;
import Models.Generators.HashGenerator;
import Models.Generators.IndexGenerator;
import Models.Objects.Message;
import Models.Objects.Session;

import javax.crypto.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class MessageGenerator
{
    public static byte[] buildSendMessage(String messageString, Session session) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
    {
        Message message = createNewMessageObject(session, messageString);
        updateSession(session, message);
        byte[] messageBytes = getMessageByteArray(message);
        byte[] finalString = EncryptionGenerator.encryptMessage(session, messageBytes);
        System.out.println("final string length after sym key encryption: " + finalString.length);
        return finalString;
    }

    private static byte[] getMessageByteArray(Message message)
    {
        //Message to byte[]
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;

        byte[] messageBytes = new byte[0];
        try
        {
            out = new ObjectOutputStream(bos);
            out.writeObject(message);
            out.flush();
            messageBytes = bos.toByteArray();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                bos.close();

            } catch (IOException ex)
            {
                // ignore close exception
            }
        }
        return messageBytes;
    }

    private static Message createNewMessageObject(Session session, String message) throws NoSuchAlgorithmException
    {
        //Generate next index and tag
        int newIndex = IndexGenerator.generateIndex();
        String newTag = Base64.getEncoder().encodeToString(HashGenerator.generateRandomString());
        return new Message(message, newTag, newIndex);
    }

    private static void updateSession(Session session, Message message)
    {
        session.setNextIndex(message.getIndex());
        session.setNextTag(message.getTag());
    }


    public static Message buildReceiveMessage(byte[] messageBytes)
    {
        ByteArrayInputStream bis = new ByteArrayInputStream(messageBytes);
        Message m = new Message();
        try (ObjectInput in = new ObjectInputStream(bis)) //TODO kijken of het hier mislooopt
        {
            m = (Message) in.readObject();
        } catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return m;
    }


}
