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
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;

import static application.Logija.logija;


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

    @FXML
    private Button btnKinnitaEemaldaKasutaja;

    @FXML
    private Button btnKustutaLogi;

    @FXML
    private Button btnNäitaLogi;

    @FXML
    private Tab tabLogiandmed;

    @FXML
    private TextArea textAreaLogi;

    @FXML
    private Button btnKinnitaÜlekanne;


    // Et saaks LoginControlleris seda controllerit initalizeda ja sealt kasutaja andmeid saata.
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        raamistik.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number vanaLaius, Number uusLaius) {
                double raamiLaius = (double) uusLaius;

                raamistik.setPrefWidth(raamiLaius);

                // Nupu suurus muutub laiusega.
                logiväljaNupp.setPrefWidth(raamiLaius * 0.15);

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
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number vanaKõrgus, Number uusKõrgus) {
                double kõrgus = (double) uusKõrgus;
                raamistik.setPrefHeight(kõrgus);
            }
        });

        txtKasutajanimiEemaldaKasutaja.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number vanaLaius, Number uusLaius) {
                double laius = (double) uusLaius;
                // Tekstiribaga sama lai nupp.
                btnKinnitaEemaldaKasutaja.setPrefWidth(laius);
            }
        });

        txtKasutajanimiLisaKasutaja.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number vanaLaius, Number uusLaius) {
                double laius = (double) uusLaius;
                // Tekstiribaga sama lai nupp.
                btnKinnitaLisaKasutaja.setPrefWidth(laius);
            }
        });
    }

    public void NäitaLogi(ActionEvent actionEvent) {
        File fail = new File("logi.log");
        try (BufferedReader lugeja = new BufferedReader(new InputStreamReader(new FileInputStream(fail)))) {
            String rida;
            while ((rida = lugeja.readLine()) != null) {
                textAreaLogi.appendText(rida + "\n");
            }
        } catch (IOException e) {
            System.out.println("Viga logi lugemisel...");
        }
    }

    public void KustutaLogi(ActionEvent actionEvent) {
        // Kustutab logifaili ära ja loob uue ja alustab logimist.
        try {
            // Sisuliselt kirjutab faili üle.
            PrintWriter kirjutaja = new PrintWriter("logi.log");
            kirjutaja.close();
        } catch (IOException e) {
            System.out.println("Logi kustutamine ebaõnnestus!");
        }

        textAreaLogi.setText("");
    }

    // Eeemaldab kasutaja andmebaasist
    public void EemaldaKasutaja(ActionEvent actionEvent) {
        if (!txtKasutajanimiEemaldaKasutaja.getText().strip().isEmpty()) {
            String eemaldadaKasutajaNimi = txtKasutajanimiEemaldaKasutaja.getText();
            String error = "";
            try {
                error = Loogika.eemaldaKasutaja(eemaldadaKasutajaNimi);
            } catch (SQLException e) {
                error = "Viga andmebaasist kasutaja eemaldamisel";
            }
            // Kui error on tühi ehk ühtegi viga ei esinenud
            if (error.isEmpty()) {
                txtErrorLisaKasutaja.setText("Kasutaja edukalt eemaldatud!");
            } else {
                Loogika.buttonColorChange(btnKinnitaEemaldaKasutaja, Color.rgb(255, 0, 0), Color.rgb(86, 168, 255));
                txtErrorEemaldaKasutaja.setText(error);
            }
        } else {
            txtErrorEemaldaKasutaja.setText("Tühjad sisendid!");
            Loogika.buttonColorChange(btnKinnitaEemaldaKasutaja, Color.rgb(255, 0, 0), Color.rgb(86, 168, 255));
        }
    }

    //Lisab Andmebaasi uue kasutaja.
    public void LisaKasutaja(ActionEvent actionEvent) {
        if (!txtKasutajanimiLisaKasutaja.getText().strip().isEmpty() &&
                !txtParoolLisaKasutaja.getText().strip().isEmpty() &&
                !txtKontonumberLisaKasutaja.getText().strip().isEmpty()) {

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
                Loogika.buttonColorChange(btnKinnitaLisaKasutaja, Color.rgb(255, 0, 0), Color.rgb(86, 168, 255));
            }
            // Kui error on tühi ehk ühtegi viga ei esinenud.
            if (error.isEmpty()) {
                txtErrorLisaKasutaja.setText("Ülekanne õnnestus!");
            } else {
                txtErrorLisaKasutaja.setText(error);
                Loogika.buttonColorChange(btnKinnitaLisaKasutaja, Color.rgb(255, 0, 0), Color.rgb(86, 168, 255));
            }
        } else {
            txtErrorLisaKasutaja.setText("Tühjad sisendid!");
            Loogika.buttonColorChange(btnKinnitaLisaKasutaja, Color.rgb(255, 0, 0), Color.rgb(86, 168, 255));
        }
    }

    public void Ülekanne(ActionEvent actionEvent) {
        // Kui antakse tühjad sisendid.
        if (!txtÜlekanneError.getText().strip().isEmpty() && !txtSumma.getText().strip().isEmpty()) {
            String kontoNumber = txtÜlekanneKontonumber.getText();
            double summa = Double.parseDouble(txtSumma.getText());
            System.out.println(kontoNumber);
            System.out.println(summa);

            String error = Loogika.ülekanne(kasutaja, kontoNumber, summa);

            // Kui error on tüühi ehk ühtegi viga ei esinenud
            if (error.isEmpty()) {
                txtÜlekanneError.setText("Ülekanne õnnestus!");
            } else {
                Loogika.buttonColorChange(btnKinnitaÜlekanne, Color.rgb(255, 0, 0), Color.rgb(86, 168, 255));
                txtÜlekanneError.setText(error);
            }
        } else {
            txtÜlekanneError.setText("Tühjad sisendid!");
            Loogika.buttonColorChange(btnKinnitaÜlekanne, Color.rgb(255, 0, 0), Color.rgb(86, 168, 255));

        }
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
            tabpaneMain.getTabs().remove(tabLogiandmed);
        }
    }

    // Logib kasutajast välja, sulgeb akna ja avab sisselogimise akna.
    public void LogOut(ActionEvent actionEvent) throws Exception {
        logija.info("Kasutaja " + kasutaja.getKasutajanimi() + " logis välja.");
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
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


