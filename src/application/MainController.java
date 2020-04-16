package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
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
    private Label lblMuutuja;

    @FXML
    private Label lblMuutujaTekst;

    @FXML
    private Label lblKontoNumber;

    @FXML
    private Label lblNumbriTekst;

    @FXML
    private Label lblKontoJääk;

    @FXML
    private Label lblJäägiTekst;

    @FXML
    private AnchorPane raamistik;

    @FXML
    private Button logiväljaNupp;

    @FXML
    private Line pikkJoon;

    @FXML
    private GridPane andmed;



    // Et saaks LoginControlleris seda controllerit initalizeda ja sealt kasutaja andmeid saata.
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        raamistik.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number vanaLaius, Number uusLaius) {
                double raamiLaius = (double) uusLaius;
                raamistik.setPrefWidth(raamiLaius);

                // Nupu suurus muutub laiusega.
                logiväljaNupp.setPrefWidth(raamiLaius*0.15);

                // Tekstide suurused
                lblKontoJääk.setFont(new Font("Arial Rounded MT BOLD", raamiLaius * 0.025));
                lblJäägiTekst.setFont(new Font("Arial Rounded MT BOLD", raamiLaius * 0.025));
                lblJäägiTekst.setFont(new Font("Arial Rounded MT BOLD", raamiLaius * 0.025));
                lblMuutuja.setFont(new Font("Arial Rounded MT BOLD", raamiLaius * 0.025));
                lblMuutujaTekst.setFont(new Font("Arial Rounded MT BOLD", raamiLaius * 0.025));
                lblKontoNumber.setFont(new Font("Arial Rounded MT BOLD", raamiLaius * 0.025));
                lblNumbriTekst.setFont(new Font("Arial Rounded MT BOLD", raamiLaius * 0.025));

                // Muudab sinise joone pikkuse akna laiuseks.
                pikkJoon.setEndX(raamiLaius);
            }
        });
        raamistik.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number vanaKõrgus, Number uusKõrgus) {
                double kõrgus = (double) uusKõrgus;
                raamistik.setPrefHeight(kõrgus);
            }
        });


    }

    // Teeb kasutaja LoginControllerist saadetud andmetega.
    public void looKasutaja(String kasutajaNimi) throws Exception {
        kasutaja = andmebaas.looKasutaja(kasutajaNimi);
        txtMainKasutajaNimi.setText(kasutajaNimi);
        lblKontoNumber.setText(kasutaja.getKontoNr());
        lblKontoJääk.setText(Double.toString(kasutaja.getKontojääk()));
    }

    // Logib kasutajast välja, sulgeb akna ja avab sisselogimise akna.
    public void LogOut(ActionEvent actionEvent) throws Exception {
        ((Node)actionEvent.getSource()).getScene().getWindow().hide();
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource(String.format("/application/%s", "Login.fxml")));
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
