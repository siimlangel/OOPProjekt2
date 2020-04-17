package application;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;


public class LoginController {
    AndmebaasiUhendaja andmebaas = new AndmebaasiUhendaja();

    @FXML
    private Label lblStaatus;

    @FXML
    private TextField txtKasutajaNimi;

    @FXML
    private TextField txtParool;

    @FXML
    private Button btnLogin;


    /**
     * Logib sisse. Kui õnnestub vahetab Main.fxml aknasse.
     * @param actionEvent event kui nuppu vajutati mille küljes see meetod on
     * @throws Exception
     */
    @FXML
    public void Login(ActionEvent actionEvent) throws Exception {

        boolean kasutajaNimiÕige = false;
        boolean paroolÕige = false;

        // Kontrolli kasutajanime andmebaasist
        if (andmebaas.testLogimine("SELECT * FROM kontod WHERE kasutajanimi = '"
        + txtKasutajaNimi.getText() + "'", txtKasutajaNimi.getText(), "kasutajanimi")) {
            kasutajaNimiÕige = true;
        }

        // Kontrolli parooli andmebaasist
        if(andmebaas.testLogimine("SELECT * FROM kontod WHERE kasutajanimi = '"
                + txtKasutajaNimi.getText() + "'", txtParool.getText(), "parool")) {
            paroolÕige = true;
        }

        // Kui sisselogimisandmed on õiged
        if (kasutajaNimiÕige && paroolÕige) {

            lblStaatus.setText("Sisselogimine õnnestus!");
            // Sulgeb sisslogimise akna.
            ((Node) actionEvent.getSource()).getScene().getWindow().hide();

            // Teeb uue stage Main.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/Main.fxml"));
            Parent root = (Parent) loader.load();
            // Instantiateb järgmise controlleri ja annab talle kasutajanime et luua seal kasutaja.
            MainController mainController = loader.getController();
            mainController.looKasutaja(txtKasutajaNimi.getText());
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            Stage primaryStage = new Stage();
            primaryStage.setScene(scene);
            primaryStage.show();

        } else {
            lblStaatus.setText("Sisselogimine ebaõnnestus!");
            buttonColorChange(btnLogin, Color.rgb(255, 0, 0), Color.rgb(86, 168, 255));
        }
    }

    /**
     * Muudab nupu värvi
     * @param btn Nupp mille värvi muuta
     * @param fromCol Mis värvist alustab
     * @param toCol Mis värviks muutub
     */
    public void buttonColorChange(Button btn, Color fromCol, Color toCol) {
        Rectangle rect = new Rectangle();
        rect.setFill(fromCol);
        FillTransition ftr = new FillTransition();
        ftr.setShape(rect);
        ftr.setDuration(Duration.millis(500));
        ftr.setFromValue(fromCol);
        ftr.setToValue(toCol);
        ftr.setInterpolator(new Interpolator() {
            @Override
            protected double curve(double v) {
                btn.setBackground(new Background(new BackgroundFill(rect.getFill(), CornerRadii.EMPTY, Insets.EMPTY)));
                return v;
            }
        });
        ftr.play();
    }




}
