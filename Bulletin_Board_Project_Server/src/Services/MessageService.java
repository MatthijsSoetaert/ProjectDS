package Services;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;

public interface MessageService extends Remote {
    void sendMessage(int i, byte[] v, String t) throws RemoteException;
    byte[] getMessage(int i, String b) throws RemoteException, InterruptedException, NoSuchAlgorithmException;
    void waitForMessage(int i, String b) throws RemoteException, InterruptedException;
}