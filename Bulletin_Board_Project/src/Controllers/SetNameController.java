package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SetNameController {

    @FXML
    public Button confirmNameButton;

    @FXML
    public TextField nameField;

    @FXML
    public void confirmName() throws IOException
    {
        setNameTextField();
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    public void setNameTextField() throws IOException
    {
        MainController.name = nameField.getText();
    }
}
