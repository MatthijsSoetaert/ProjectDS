package Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parameters params = getParameters();
        List<String> list = params.getRaw();
        Parent root = FXMLLoader.load(getClass().getResource("../Views/Password.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle(list.get(0));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
