package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;



public class ÜlekanneController implements Initializable {

    @FXML
    private BorderPane raam;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    @FXML
    private void Tagasi(ActionEvent event)throws Exception {

        Parent teineVaade = FXMLLoader.load(getClass().getResource(String.format("/application/%s", "Main.fxml")));

        Scene tagasi = new Scene(teineVaade, raam.getWidth(), raam.getHeight());
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tagasi);
        window.show();
        window.setMinWidth(600);
        window.setMinHeight(450);

    }
}
