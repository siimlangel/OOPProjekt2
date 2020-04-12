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
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Time;

public class MainController {

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
    public void Login(ActionEvent actionEvent) throws Exception{
        // Kui sisselogimise andmed on õiged
        // Siia saaks eelmisest projektist sisselogimise ümber teha boolean methodina.
        // username tuleb txtKasutajaNimi.getText()-ist
        // parool tuleb txtParool.getText()-ist-
        if (txtKasutajaNimi.getText().equals("user") &&
            txtParool.getText().equals("parool")) {

            lblStaatus.setText("Sisse logimine õnnestus!");
            // Sulgeb sisslogimise akna.
            ((Node) actionEvent.getSource()).getScene().getWindow().hide();


            // Teeb uue stage
            createStage("Main.fxml");


        } else {
            lblStaatus.setText("Sisse logimine ebaõnnestus!");
            buttonColorChange(btnLogin, Color.rgb(255, 0, 0), Color.rgb(86, 168, 255));
        }
    }

    /**
     * Sulgeb eelmise akna ja läheb ülekande stage
     * @param actionEvent event kui nuppu vajutati
     * @throws Exception
     */
    public void toUlekanne(ActionEvent actionEvent) throws Exception {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
        createStage("Ulekanne.fxml");
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

    /**
     * Teeb uue stage
     * @param fileFXML mis stage tekitada
     * @throws Exception
     */
    public void createStage(String fileFXML) throws Exception{
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource(String.format("/application/%s", fileFXML)));
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Liigub Main.fxml aknasse
     * @param actionEvent event kui vajutati nuppu mille küljes see meetod on
     * @throws Exception
     */
    public void toMain(ActionEvent actionEvent) throws Exception {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
        createStage("Main.fxml");
    }


}
