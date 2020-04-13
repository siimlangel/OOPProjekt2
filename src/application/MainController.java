package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class MainController implements Initializable {
    private Kasutaja kasutaja;
    private AndmebaasiUhendaja andmebaas = new AndmebaasiUhendaja();

    @FXML
    private Text txtMainKasutajaNimi;

    @FXML
    private Label lblKontoNumber;

    @FXML
    private Label lblKontoJääk;


    // Et saaks LoginControlleris seda controllerit initalizeda ja sealt kasutaja andmeid saata.
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    // Logib kasutajast välja, sulgeb akna ja avab sisselogimise akna.
    public void LogOut(ActionEvent actionEvent) throws Exception {
        ((Node)actionEvent.getSource()).getScene().getWindow().hide();
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource(String.format("/application/%s", "Login.fxml")));
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Teeb kasutaja LoginControllerist saadetud andmetega.
    public void looKasutaja(String kasutajaNimi) throws Exception {
        kasutaja = andmebaas.looKasutaja(kasutajaNimi);
        txtMainKasutajaNimi.setText(kasutajaNimi);
        lblKontoNumber.setText(kasutaja.getKontoNr());
        lblKontoJääk.setText(Double.toString(kasutaja.getKontojääk()));
    }


}
