package Controllers;

import Models.Generators.KeyGenerator;
import Models.Objects.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class NewSessionController
{
    @FXML
    ComboBox<String> symEncryptionTypeDropdown;

    @FXML
    ComboBox<String> hashingAlgorithmDropDown;

    @FXML
    ComboBox<String> symEncryptionLengthDropDown;

    @FXML
    TextArea sendingSymmetricKeyField;

    @FXML
    Button generateKeyButton;

    @FXML
    TextField sendingFirstIndexTextField;

    @FXML
    TextField sendingFirstTagTextField;

    @FXML
    Button indexAndTagGenerateButton;

    @FXML
    Button createSessionButton;

    @FXML
    TextField nameField;

    @FXML
    TextArea receivingSymKey;

    @FXML
    TextField receivingFirstTag;

    @FXML
    TextField receivingFirstIndex;

    ObservableList<String> aesLengthOptions =
            FXCollections.observableArrayList(
                  /*  "128",
                    "192",*/
                    "256"
            );

    ObservableList<String> desLengthOptions =
            FXCollections.observableArrayList(
                    "64"
            );

    ObservableList<String> symKeyTypeOptions =
            FXCollections.observableArrayList(
                    "AES"
                    // "DES"
            );

    ObservableList<String> hashingOptions =
            FXCollections.observableArrayList(
                    //  "MD5",
                    //  "SHA1",
                    "SHA256"
            );

    private MainController mainController;

    @FXML
    private void initialize()
    {
        symEncryptionTypeDropdown.setItems(symKeyTypeOptions);
        hashingAlgorithmDropDown.setItems(hashingOptions);
    }

    @FXML
    public void generateSymmetricKey() throws NoSuchAlgorithmException
    {
        String symKeyType = symEncryptionTypeDropdown.getValue();
        int symKeyLength = Integer.parseInt(symEncryptionLengthDropDown.getValue());
        SecretKey secretKey = KeyGenerator.generateSymmetricKey(symKeyType, symKeyLength);
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        sendingSymmetricKeyField.setText(encodedKey);
    }

    @FXML
    public void generateTagAndIndex()
    {
        generateTag();
        generateIndex(Integer.MAX_VALUE);
    }

    private void generateTag()
    {
        StringBuilder sb = new StringBuilder();
        int length = 100;
        switch (hashingAlgorithmDropDown.getValue())
        {
            case "MD5":
                length = 128;
                break;
            case "SHA1":
                length = 160;
                break;
            case "SHA256":
                length = 256;
                break;
            default:
                length = 256;
                break;
        }

        for (int i = 0; i < length; i++)
        {
            int j = 97 + (int) (Math.random() * 25);
            sb.append((char) j);
        }
        sendingFirstTagTextField.setText(sb.toString());

    }

    private void generateIndex(int max)
    {
        sendingFirstIndexTextField.setText(Integer.toString((int) (Math.random() * max)));
    }

    @FXML
    public void setSymKeyLengths()
    {
        switch (symEncryptionTypeDropdown.getValue())
        {
            case "AES":
                symEncryptionLengthDropDown.setItems(aesLengthOptions);
                break;
           /* case "DES":
                symEncryptionLengthDropDown.setItems(desLengthOptions);
                break;*/

        }
    }

    @FXML
    public void createSession() throws IOException, NotBoundException, InterruptedException
    {
        if (fieldsFilledIn())
        {
            Session session = createNewSession();
            MainController.getSessions().put(nameField.getText(), session);
            setName();
            addNewTab(session.getName());
            instantiateListening();
            Stage stage = (Stage) generateKeyButton.getScene().getWindow();
            stage.close();
        }
    }

    private boolean fieldsFilledIn()
    {
        return !sendingFirstIndexTextField.getText().equals("")
                && !sendingFirstTagTextField.getText().equals("")
                && !sendingSymmetricKeyField.getText().equals("");
    }

    private void addNewTab(String name) throws IOException
    {
        Tab t = (Tab) FXMLLoader.load(this.getClass().getResource("../Views/TabTemplate.fxml"));
        t.setText(name);
        mainController.getTabPane().getTabs().add(t);
        //setSendName();

    }

    private void instantiateListening() throws IOException, InterruptedException
    {
        mainController.receiveMessage(nameField.getText());
    }

    private void setName()
    {
        mainController.setUserName(MainController.name);
    }

    private Session createNewSession()
    {
        Session s = new Session(sendingSymmetricKeyField.getText(), sendingFirstTagTextField.getText(), Integer.parseInt(sendingFirstIndexTextField.getText()), nameField.getText());
        s.setReceiveIndex(Integer.parseInt(receivingFirstIndex.getText()));
        s.setReceiveSymKey(receivingSymKey.getText());
        s.setReceiveTag(receivingFirstTag.getText());
        return s;
    }

    public void setMainController(MainController mainController)
    {
        this.mainController = mainController;
    }

}
