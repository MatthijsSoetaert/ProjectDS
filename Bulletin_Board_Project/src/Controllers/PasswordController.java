package Controllers;

import Models.Generators.HashGenerator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class PasswordController
{

    @FXML
    public Label ErrorMessageTextField;

    @FXML
    public TextField passwordTextField;

    private String pathToFile = "C:\\Users\\matth\\OneDrive\\Documenten\\School\\Gedistribueerde systemen\\Labo\\Bulletin_Board_Project\\src\\Resources\\password.txt";
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public void confirmPassword() throws IOException, NoSuchAlgorithmException
    {
        String hashedPassword = getHashedPasswordFromFile();
        String inputString = generateHashedPassword();
        checkIfValidPassword(hashedPassword,inputString);
    }

    private void checkIfValidPassword(String s1, String s2) throws IOException
    {
        if(s1.equals(s2)){
            openMainScreen();
        }
        else {
            ErrorMessageTextField.setText("Wrong password");
        }
    }

    private void openMainScreen() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/MainScreen.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root, 1100, 600));
        stage.show();
        closeThisWindow();

    }

    private void closeThisWindow(){
        Stage currentStage = (Stage) ErrorMessageTextField.getScene().getWindow();
        currentStage.close();
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
    private String  generateHashedPassword() throws NoSuchAlgorithmException
    {
        byte[] inputHash = HashGenerator.generateHash(passwordTextField.getText());
        return bytesToHex(inputHash);

    }
    private String getHashedPasswordFromFile(){
        String data = "";
        try {
            File myObj = new File(pathToFile);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }
}
