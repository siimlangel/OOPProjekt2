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
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
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
    private TextField txtÜlekanneKontonumber;

    @FXML
    private TextField txtSumma;

    @FXML
    private Text txtÜlekanneError;

    @FXML
    private Tab tabLisaKasutaja;

    @FXML
    private TabPane tabpaneMain;

    @FXML
    private TextField txtKasutajanimiLisaKasutaja;

    @FXML
    private TextField txtParoolLisaKasutaja;

    @FXML
    private TextField txtKontonumberLisaKasutaja;

    @FXML
    private CheckBox chkboxOnAdminLisaKasutaja;

    @FXML
    private Button btnKinnitaLisaKasutaja;

    @FXML
    private Text txtErrorLisaKasutaja;

    @FXML
    private Tab tabEemaldaKasutaja;

    @FXML
    private TextField txtKasutajanimiEemaldaKasutaja;

    @FXML
    private Text txtErrorEemaldaKasutaja;


    // Et saaks LoginControlleris seda controllerit initalizeda ja sealt kasutaja andmeid saata.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        raamistik.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number vanaLaius, Number uusLaius) {
                double raamiLaius = (double) uusLaius;

                raamistik.setPrefWidth(raamiLaius);

                // Nupu suurus muutub laiusega.
                logiväljaNupp.setPrefWidth(raamiLaius*0.15);

                // Tekstide suurused.
                lblKontoJääk.setFont(new Font("Arial Rounded MT BOLD", raamiLaius * 0.02));
                lblJäägiTekst.setFont(new Font("Arial Rounded MT BOLD", raamiLaius * 0.02));
                lblJäägiTekst.setFont(new Font("Arial Rounded MT BOLD", raamiLaius * 0.02));
                lblMuutuja.setFont(new Font("Arial Rounded MT BOLD", raamiLaius * 0.02));
                lblMuutujaTekst.setFont(new Font("Arial Rounded MT BOLD", raamiLaius * 0.02));
                lblKontoNumber.setFont(new Font("Arial Rounded MT BOLD", raamiLaius * 0.02));
                lblNumbriTekst.setFont(new Font("Arial Rounded MT BOLD", raamiLaius * 0.02));

                // Muudab sinise joone pikkuse akna laiuseks.
                pikkJoon.setStartX(0);
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

    public void EemaldaKasutaja(ActionEvent actionEvent) {
        String eemaldadaKasutajaNimi = txtKasutajanimiEemaldaKasutaja.getText();
        String error = "";
        try {
            error = Loogika.eemaldaKasutaja(eemaldadaKasutajaNimi);
        } catch (SQLException e) {
            error = "Viga andmebaasist kasutaja eemaldamisel";
        }
        txtErrorEemaldaKasutaja.setText(error);
    }

    //Lisab Andmebaasi uue kasutaja.
    public void LisaKasutaja(ActionEvent actionEvent) {
        String kasutajaNimi = txtKasutajanimiLisaKasutaja.getText();
        String parool = txtParoolLisaKasutaja.getText();
        String kontoNumber = txtKontonumberLisaKasutaja.getText();
        boolean onAdmin = chkboxOnAdminLisaKasutaja.isSelected();
        double kontoJääk = 0.0;
        String error = "";
        try {
            error = Loogika.lisaKasutaja(kasutajaNimi, parool, kontoNumber, kontoJääk, onAdmin);
        } catch (SQLException e) {
            error = "Viga andmebaasi kasutaja lisamisel";
        }
        txtErrorLisaKasutaja.setText(error);
    }

    public void Ülekanne(ActionEvent actionEvent) {
        String kontoNumber = txtÜlekanneKontonumber.getText();
        double summa = Double.parseDouble(txtSumma.getText());
        System.out.println(kontoNumber);
        System.out.println(summa);
        String ülekandeTulemus = Loogika.ülekanne(kasutaja, kontoNumber, summa);
        txtÜlekanneError.setText(ülekandeTulemus);
    }

    // Teeb kasutaja LoginControllerist saadetud andmetega.
    public void looKasutaja(String kasutajaNimi) throws Exception {
        kasutaja = andmebaas.looKasutaja(kasutajaNimi);
        txtMainKasutajaNimi.setText(kasutajaNimi);
        lblKontoNumber.setText(kasutaja.getKontoNr());
        lblKontoJääk.setText(Double.toString(kasutaja.getKontojääk()));

        // Näitab ainult administraatoritele kasutaja lisamise ja eemaldamise tabi.
        if (kasutaja instanceof Klient) {
            tabpaneMain.getTabs().remove(tabLisaKasutaja);
            tabpaneMain.getTabs().remove(tabEemaldaKasutaja);
        }
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
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(300);
    }

}


