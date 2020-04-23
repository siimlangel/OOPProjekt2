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
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

import static application.Logija.logija;

public class Loogika {
    // Programm ühendatakse andmebaasiga.
    public static AndmebaasiUhendaja andmebaas = new AndmebaasiUhendaja();
    static Scanner scanner = new Scanner(System.in);

    // Meetodiga genereeritakse kaks numbrit vahemikus 0-10, ja prinditakse liitmistehe ja tagastatakse
    // selle liitmistehte vastus sõnena, et mugavalt kontrollida seda kasutaja vastusega.
    public static String robotiKontroll() {
        int esimene = (int) (Math.random() * (11));
        int teine = (int) (Math.random() * (11));
        System.out.print("Robotikontroll! " + esimene + " + " + teine + " = ");
        String summa = String.valueOf(esimene + teine);
        return summa;
    }

    public static String eemaldaKasutaja(String kasutajanimi) throws SQLException {
        if (!andmebaas.testLogimine("select * from kontod where kasutajanimi = '" + kasutajanimi + "'", kasutajanimi, "kasutajanimi")) {
            return "Error: Sellise nimega kasutajat pole";
        } else {
            andmebaas.eemaldaKasutaja(kasutajanimi);
            logija.info("Kasutaja " + kasutajanimi + " eemaldati andmebaasist!");
            return "Kasutaja edukalt eemaldatud!";
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
            return "Kasutaja lisamine õnnestus";
        }
    }

    // Teise kasutaja või enda parooli muutmine.
    private static void muudaParooli(Kasutaja admin) throws SQLException {
        String vanaParool;
        String uusParool;
        String kasutajaNimi; // Kasutaja, kelle parooli muudetakse.

        do {
            System.out.print("Sisestage kasutajanimi, kelle parooli muuta -> ");
            kasutajaNimi = scanner.next();
            vanaParool = andmebaas.getParool(kasutajaNimi); // Võtab kasutaja vanaparooli andmebaasist.

            System.out.print("Sisestage uus parool -> ");
            uusParool = scanner.next();
            if (uusParool.equals(vanaParool)) {
                System.out.println("Uus parool ei saa olla sama, mis praegune...");
                continue;
            }
            if (uusParool.length() < 6) {
                System.out.println("Parool peab olema pikem kui 6 sümbolit...");
                continue;
            }

            String query = String.format("UPDATE kontod SET parool = '%s'" +
                    " WHERE kasutajanimi = '%s'", uusParool, kasutajaNimi);

            // Uuendatakse kasutaja parool andmebaasis, või kui muudetakse enda parooli, siis ajutiselt programmis.
            try {
                if (!admin.getKasutajanimi().equals(kasutajaNimi)) {
                    andmebaas.sisestaBaasi(query);
                } else { // Kui muudetakse iseenda parooli.
                    admin.setParool(uusParool);
                }

            } catch (SQLException e) {
                System.out.println("Parooli vahetamine ei õnnestunud...");
                return;
            }

        } while (uusParool.equals(vanaParool) || uusParool.length() < 6);
        System.out.println("Parooli vahetamine õnnestus!");
    }


    // Raha välja võtmine.
    private static void rahaVälja(Kasutaja kasutaja) {
        System.out.print("Sisesta summa, mida soovid välja võtta, sente ei väljastata: ");

        int summa = scanner.nextInt();

        if (summa <= kasutaja.getKontojääk()) {
            kasutaja.setKontojääk(kasutaja.getKontojääk() - summa);
            System.out.println("Palun, siin on teile " + summa + " eurot!");
        } else {
            System.out.println("Teie kontol puuduvad piisavad vahendid.");
        }

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
                return "Ülekanne õnnestus!";
            } else {
                return "Sobimatu pangakonto number...";
            } // Sellega kontrollib, kas kontonumber on olemas, erind kui ei ole.


        } catch (SQLException e) {
            return "Ülekanne ebaõnnestus...";
        }
    }

}




