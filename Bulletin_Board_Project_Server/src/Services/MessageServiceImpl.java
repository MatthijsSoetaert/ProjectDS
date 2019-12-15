package Services;

import MainPackage.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageServiceImpl extends UnicastRemoteObject implements MessageService
{

    public MessageServiceImpl() throws RemoteException
    {
    }


    @Override
    public synchronized void sendMessage(int i, byte[] v, String t) throws RemoteException
    {
        List<Map<String, byte[]>> mailBoxes = BulletinBoard.getMailBoxes();
        System.out.println("message " + v);
        mailBoxes.get(i % BulletinBoard.getSize()).put(t, v);
        System.out.println("\n\n####NEW SENDING MESSAGE####");
        System.out.println("Putting message:" + v + " on place:" + i + "and tag: " + t);
        notifyAll();
    }

    @Override
    public synchronized byte[] getMessage(int i, String b) throws RemoteException, InterruptedException, NoSuchAlgorithmException
    {
        List<Map<String, byte[]>> mailBoxes = BulletinBoard.getMailBoxes();
        Map<String, byte[]> pairs = mailBoxes.get(i % BulletinBoard.getSize());


        byte[] message = null;
        while (message == null)
        {
            wait();
            for (String t : pairs.keySet())
            {
                String hashedTag = new String(HashGenerator.generateHash(b));
                if (t.equals(hashedTag))
                {
                    message = pairs.remove(t);;
                }
            }
        }
        System.out.println("\n\n####NEW RECEIVING MESSAGE####");
        System.out.println("Listening to message from index: " + i + " and \n tag: " + b);
        return message;
    }

    @Override
    public void waitForMessage(int i, String b) throws RemoteException, InterruptedException
    {

    }
}