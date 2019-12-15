package Controllers;

import Models.Generators.EncryptionGenerator;
import Models.Generators.HashGenerator;
import Models.Generators.KeyGenerator;
import Models.Generators.MessageGenerator;
import Models.Objects.Message;
import Models.Objects.Session;
import Services.MessageService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;


public class MainController
{

    @FXML
    TextField insertTextField;

    @FXML
    Label nameTextField;

    @FXML
    Button sendButton;

    @FXML
    MenuItem createSessionItem;

    @FXML
    TabPane tabPane;

    @FXML
    TextArea messagesTextField;

    @FXML
    MenuBar menuBar;

    private static Map<String, Session> sessions = new HashMap<>();

    public static String name = "Unnamed";

    // Add a public no-args constructor
    public MainController()
    {
    }

    @FXML
    private void initialize()
    {

        if (tabPane != null)
        {

            tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) ->
            {
                System.out.println("switched to : " + newTab.getText());
                String text = sessions.get(newTab.getText()).getMessageBoard();
                Scene scene = tabPane.getScene();
                messagesTextField = (TextArea) scene.lookup("#messagesTextField");
                messagesTextField.setText(text);            });
        }
    }

    public TabPane getTabPane()
    {
        return tabPane;
    }

    //CREATE SESSION
    @FXML
    public void openCreateSessionInstance() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/NewSession.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        NewSessionController newSessionController = loader.getController();
        newSessionController.setMainController(this);
        stage.show();

        System.out.println("een test");
    }

    @FXML
    public void setName() throws IOException
    {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../Views/SetName.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void sendMessage()
    {
        try
        {
            MessageService impl = getMessageService();
            Scene scene = insertTextField.getScene();
            TabPane tb = (TabPane) scene.lookup("#tabPane");
            String tabName = tb.getSelectionModel().getSelectedItem().getText();

            Session session = getSessionByName(tabName);

            String message = insertTextField.getText();

            session.addSendMessageToBoard(message, name);

            System.out.println("-------------------------------------------------------------------------");
            System.out.println("Sent message: " + session.getMessageBoard());
            System.out.println("-------------------------------------------------------------------------");

            addMessageToViewWhenSent(session.getMessageBoard());
            byte[] finalMessage = MessageGenerator.buildSendMessage(message, session);

            String hashedTag = new String(HashGenerator.generateHash(session.getSendTag()));
            impl.sendMessage(session.getSendIndex(), finalMessage, hashedTag);

            updateSessionPropertiesAfterSend(session);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @FXML
    public void receiveMessage(String to)
    {
        new Thread(() ->
        {
            try
            {
                MessageService impl = getMessageService();

                Session session = getSessionByName(to);

                byte[] s = impl.getMessage(session.getReceiveIndex(), session.getReceiveTag());

                SecretKey symKey = KeyGenerator.recreateKey(session.getReceiveSymKey());
                byte[] decryptedMessage = EncryptionGenerator.decryptMessage(symKey, s);
                Message m = MessageGenerator.buildReceiveMessage(decryptedMessage);

                session.addReceivedMessagesToBoard(m.getMessage());

                String tabName = tabPane.getSelectionModel().getSelectedItem().getText();
                if (tabName.equals(session.getName()))
                {
                    addMessageToViewWhenReceived(session.getMessageBoard());
                }
                System.out.println("-------------------------------------------------------------------------");
                System.out.println("Received message: " + session.getMessageBoard());
                System.out.println("-------------------------------------------------------------------------");

                updateSessionPropertiesAfterReceive(session, m);
                receiveMessage(to);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }).start();
    }


    private MessageService getMessageService() throws RemoteException, NotBoundException
    {
        Registry myRegistry = LocateRegistry.getRegistry("localhost", 1199);
        return (MessageService) myRegistry.lookup("MessageService");
    }

    private Session getSessionByName(String name)
    {
        for (String s : sessions.keySet())
        {
            if (s.equals(name)) return sessions.get(s);
        }
        return null;
    }

    private void addMessageToViewWhenSent(String message)
    {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {

                messagesTextField.setText(message);
            }
        });

    }

    private void addMessageToViewWhenReceived(String message)
    {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                Scene scene = nameTextField.getScene();
                messagesTextField = (TextArea) scene.lookup("#messagesTextField");
                messagesTextField.setText(message);
            }
        });

    }

    private void updateSessionPropertiesAfterSend(Session session) throws InvalidKeySpecException, NoSuchAlgorithmException
    {
        session.setSendIndex(session.getNextIndex());
        session.setSendTag(session.getNextTag());
        session.setSendSymKey(KeyGenerator.deriveKey(session.getSendSymKey()));
    }

    private void updateSessionPropertiesAfterReceive(Session session, Message message) throws InvalidKeySpecException, NoSuchAlgorithmException
    {
        session.setReceiveIndex(message.getIndex());
        session.setReceiveTag(message.getTag());
        session.setReceiveSymKey(KeyGenerator.deriveKey(session.getReceiveSymKey()));
    }

    public void setUserName(String text)
    {

        name = text;
        Platform.runLater(() ->
        {
            Scene scene = tabPane.getScene();
            nameTextField = (Label) scene.lookup("#nameTextField");
            nameTextField.setText(text);
        });
    }


    //GETTERS AND SETTERS
    public static Map<String, Session> getSessions()
    {
        return sessions;
    }

}
