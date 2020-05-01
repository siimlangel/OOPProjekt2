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
import static application.Main.andmebaas;


public class MainController implements Initializable {
    private Kasutaja kasutaja;

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
    private Tab tabLogiandmed;

    @FXML
    private TextArea textAreaLogi;

    @FXML
    private Button btnKinnitaÜlekanne;

    @FXML
    private TextField txtVanaParool;

    @FXML
    private TextField txtUusParool;

    @FXML
    private TextField txtKinnitaParool;

    @FXML
    private Text txtErrorMuudaParool;

    @FXML
    private Button btnMuudaParool;


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
                btnKinnitaEemaldaKasutaja.setPrefWidth(laius*0.80);
            }
        });

        txtKasutajanimiLisaKasutaja.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number vanaLaius, Number uusLaius) {
                double laius = (double) uusLaius;
                // Tekstiribaga sama lai nupp.
                btnKinnitaLisaKasutaja.setPrefWidth(laius*0.80);
            }
        });

        txtVanaParool.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number vanaLaius, Number uusLaius) {
                double laius = (double) uusLaius;
                // Tekstiribaga sama lai nupp.
                btnMuudaParool.setPrefWidth(laius*0.80);
            }
        });
    }

    public void NäitaLogi(ActionEvent actionEvent) {

        // Nullin enne logiväljundi ära.
        textAreaLogi.setText("");

        // Loen logifailist info sisse.
        File fail = new File("logike.log");

        try (BufferedReader lugeja = new BufferedReader(new InputStreamReader(new FileInputStream(fail)))) {
            String rida;
            while ((rida = lugeja.readLine()) != null) {
                textAreaLogi.appendText(rida + "\n");
            }
        } catch (IOException e) {
            // Kui ei õnnestu logi lugeda.
            textAreaLogi.setText("Viga logi lugemisel...");
        }
    }

    public void KustutaLogi(ActionEvent actionEvent) {
        // Kustutab logifaili ära ja loob uue ja alustab logimist.
        try {
            // Sisuliselt kirjutab faili üle.
            PrintWriter kirjutaja = new PrintWriter("logike.log");
            kirjutaja.close();
        } catch (IOException e) {
            textAreaLogi.setText("Logi kustutamine ebaõnnestus!");
        }

        textAreaLogi.setText("");
    }

    public void MuudaParool(ActionEvent actionEvent) {
        String vanaParool = txtVanaParool.getText();
        String uusParool = txtUusParool.getText();
        String kinnitaParool = txtKinnitaParool.getText();
        String error = "";

        try {
            error = Loogika.muudaParooli(kasutaja, vanaParool, uusParool, kinnitaParool);
        } catch (SQLException e) {
            error = "Viga andmebaasis parooli muutmisel...";
        }

        if (error.isEmpty()) {
            txtErrorMuudaParool.setText("Parool edukalt muudetud!");
        } else {
            Loogika.buttonColorChange(btnMuudaParool, Color.rgb(255, 0, 0), Color.rgb(86, 168, 255));
            txtErrorMuudaParool.setText(error);
        }

    }

    // Eemaldab kasutaja andmebaasist
    public void EemaldaKasutaja(ActionEvent actionEvent) {

            String eemaldadaKasutajaNimi = txtKasutajanimiEemaldaKasutaja.getText();
            String error = "";
            try {
                error = Loogika.eemaldaKasutaja(eemaldadaKasutajaNimi);
            } catch (SQLException e) {
                error = "Viga andmebaasist kasutaja eemaldamisel...";
            }
            // Kui error on tühi ehk ühtegi viga ei esinenud
            if (error.isEmpty()) {
                txtErrorEemaldaKasutaja.setText("Kasutaja eemaldamine õnnestus!");
            } else {
                Loogika.buttonColorChange(btnKinnitaEemaldaKasutaja, Color.rgb(255, 0, 0), Color.rgb(86, 168, 255));
                txtErrorEemaldaKasutaja.setText(error);
            }
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
                error = "Viga andmebaasi kasutaja lisamisel!";
                Loogika.buttonColorChange(btnKinnitaLisaKasutaja, Color.rgb(255, 0, 0), Color.rgb(86, 168, 255));
            }
            // Kui error on tühi ehk ühtegi viga ei esinenud.
            if (error.isEmpty()) {
                txtErrorLisaKasutaja.setText("Kasutaja lisatud!");
            } else {
                txtErrorLisaKasutaja.setText(error);
                Loogika.buttonColorChange(btnKinnitaLisaKasutaja, Color.rgb(255, 0, 0), Color.rgb(86, 168, 255));
            }
    }

    public void Ülekanne(ActionEvent actionEvent) {

        try {
            String kontoNumber = txtÜlekanneKontonumber.getText();
            double summa = Double.parseDouble(txtSumma.getText());

            String error = Loogika.ülekanne(kasutaja, kontoNumber, summa);

            // Kui error on tühi ehk ühtegi viga ei esinenud
            if (error.isEmpty()) {
                txtÜlekanneError.setText("Ülekanne õnnestus!");
                kasutaja.setKontojääk(Math.round((kasutaja.getKontojääk() - summa) * 100.0) / 100.0);
                lblKontoJääk.setText(String.valueOf(kasutaja.getKontojääk()));
            } else {
                Loogika.buttonColorChange(btnKinnitaÜlekanne, Color.rgb(255, 0, 0), Color.rgb(86, 168, 255));
                txtÜlekanneError.setText(error);
            }
            // Püüame kinni, kui sisend pole arvulisel kujul, või on muud moodi sobimatu, või näiteks tühi.
        } catch (NumberFormatException e) {
            Loogika.buttonColorChange(btnKinnitaÜlekanne, Color.rgb(255, 0, 0), Color.rgb(86, 168, 255));
            txtÜlekanneError.setText("Sobimatu sisend!");
        }
    }

    // Teeb kasutaja LoginControllerist saadetud andmetega.
    public void looKasutaja(String kasutajaNimi) throws Exception {
        kasutaja = andmebaas.looKasutaja(kasutajaNimi);
        txtMainKasutajaNimi.setText(kasutajaNimi);
        lblKontoNumber.setText(kasutaja.getKontoNr());
        lblMuutuja.setText("Admin");
        lblKontoJääk.setText(Double.toString(kasutaja.getKontojääk()));

        // Näitab ainult administraatoritele kasutaja lisamise ja eemaldamise tabi.
        if (kasutaja instanceof Klient) {
            lblMuutuja.setText("Tavakasutaja");
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
        primaryStage.setTitle("Sisselogimine");
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(300);
    }

}


