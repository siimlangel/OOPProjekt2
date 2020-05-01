package application;

import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.*;

import static application.Logija.logija;
import static application.Main.andmebaas;

public class Loogika {

    // Kasutaja andmebaasist eemaldamine
    public static String eemaldaKasutaja(String kasutajanimi) throws SQLException {
        if (!andmebaas.testLogimine("select * from kontod where kasutajanimi = '" + kasutajanimi + "'", kasutajanimi, "kasutajanimi")) {
            return "Sellist kasutajat ei ole!";
        } else {
            andmebaas.eemaldaKasutaja(kasutajanimi);
            logija.info("Kasutaja " + kasutajanimi + " eemaldati andmebaasist!");
            return "";
        }
    }

    // Kasutaja andmebaasi lisamine
    public static String lisaKasutaja(String kasutajanimi, String parool, String kontonumber, double kontojääk, boolean onAdmin) throws SQLException {
        boolean korrasSisestus = true;
        String error = "";

        if (kasutajanimi.length() < 5) {
            error += "Error: Kasutajanimi peab olema pikem kui 5 sümbolit \n";
            korrasSisestus = false;
        }
        if (parool.length() < 6) {
            error += "Error: Parool peab olema pikem kui 6 sümbolit \n";
            korrasSisestus = false;
        }
        if (andmebaas.testLogimine("select * from kontod where kasutajanimi = '" + kasutajanimi + "'", kasutajanimi, "kasutajanimi")) {
            error += "Error: Sellise nimega kasutaja on juba olemas \n";
            korrasSisestus = false;
        }
        if (kontonumber.length() < 11) {
            error += "Error: Vigane kontonumber \n";
            korrasSisestus = false;
        }
        if (andmebaas.testLogimine("select * from kontod where kontonumber = '" + kontonumber + "'", kontonumber, "kontonumber")) {
            error += "Error: See kontonumber on juba kasutusel \n";
            korrasSisestus = false;
        }
        if (kontojääk < 0) {
            error += "Error: Kontojääk on negatiivne\n";
            korrasSisestus = false;
        }
        if (!korrasSisestus) {
            return error;
        } else {
            andmebaas.lisaKasutaja(kasutajanimi, parool, kontonumber, kontojääk, onAdmin);
            logija.info("Kasutaja " + kasutajanimi + " lisati andmebaasi!");
            return "";
        }
    }

    // Enda parooli muutmine.
    static String muudaParooli(Kasutaja kasutaja, String vanaParool, String uusParool, String kordus) throws SQLException {

        String kasutajaNimi; // Kasutaja, kelle parooli muudetakse.

        kasutajaNimi = kasutaja.getKasutajanimi();

        if (!vanaParool.equals(andmebaas.getParool(kasutajaNimi))) {
            return "Vana parool ei ole õige...";
        }

        if (!uusParool.equals(kordus)) {
            return "Paroolid ei kattu...";
        }

        if (uusParool.equals(vanaParool)) {
            return "Uus parool on sama, mis vana parool...";
        }

        if (uusParool.length() < 6) {
            return "Parool peab olema pikem kui 6 sümbolit...";
        }

        String query = String.format("UPDATE kontod SET parool = '%s'" +
                " WHERE kasutajanimi = '%s'", uusParool, kasutajaNimi);

        try {
            andmebaas.sisestaBaasi(query);
            logija.info("Kasutaja " + kasutajaNimi + " parool vahetati.");
        } catch (SQLException e) {
            return "Parooli vahetamine ei õnnestunud...";
        }

        return "Parooli vahetamine õnnestus!";
    }

    /**
     * Muudab nupu värvi
     * @param btn Nupp mille värvi muuta
     * @param fromCol Mis värvist alustab
     * @param toCol Mis värviks muutub
     */
    public static void buttonColorChange(Button btn, Color fromCol, Color toCol) {
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

    // Teisele kontole ülekande tegemine.
    public static String ülekanne(Kasutaja kasutaja, String kontoNr, double summa) {

        if (kontoNr.equals(kasutaja.getKontoNr())) {
            return "Saajakonto on Teie enda oma...";
        }

        if (summa > kasutaja.getKontojääk()) {
            return "Vahendid puuduvad...";
        }

        // Väldime negatiivseid või paljude komadega arve
        if (summa <= 0 || !(summa == Math.round((summa) * 100.0) / 100.0)) {
            return "Sobimatu rahaühik...";
        }

        // SQL käsklus, mis uuendab saajakonto rahalist seisu andmebaasis.
        String saajaQuery = String.format("UPDATE kontod SET kontojääk = kontojääk + %s WHERE " +
                "kontonumber = '%s'", summa, kontoNr);
        // SQL käsklus, mis uuendab kandjakonto rahalist seisu andmebaasis.
        String kandjaQuery = String.format("UPDATE kontod SET kontojääk = kontojääk - %s WHERE " +
                "kontonumber = '%s'", summa, kasutaja.getKontoNr());

        // Proovitakse käsklust andmebaasi sisestada, kui kõik läheb plaanipäraselt, siis andmebaasis muutub
        // saaja konto rahaline seisund ning kasutajalt lahutatakse vastav summa maha.
        try {
            String query2 = String.format("SELECT * from kontod WHERE kontonumber = '%s'", kontoNr);
            if (andmebaas.testLogimine(query2, kontoNr, "kontonumber")) {
                andmebaas.sisestaBaasi(saajaQuery);
                andmebaas.sisestaBaasi(kandjaQuery);
                logija.info("Kontolt " + kasutaja.getKontoNr() + " tehti ülekanne kontole " + kontoNr + "! Summa: " + summa + ".");
                // Õnnestus
                return "";
            } else {
                return "Sobimatu pangakonto number...";
            } // Sellega kontrollib, kas kontonumber on olemas, erind kui ei ole.


        } catch (SQLException e) {
            return "Ülekanne ebaõnnestus...";
        }
    }

}




