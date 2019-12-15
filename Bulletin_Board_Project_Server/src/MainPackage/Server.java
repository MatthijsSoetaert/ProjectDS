package MainPackage;

import Services.MessageServiceImpl;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class Server {
    BulletinBoard bulletinBoard;

    public static void main(String args[]) {

        Server server = new Server();
        server.init();
        server.startServer();
    }

    private void startServer() {
        try {
            // create on port 1099
            Registry registry = LocateRegistry.createRegistry(1199);
            // create a new service named CounterService
            registry.rebind("MessageService", new MessageServiceImpl());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("system is ready");
    }

    private void init(){
        bulletinBoard = new BulletinBoard(20000);
    }
}