package application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

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

    // Kasutaja andmebaasi lisamine
    public static void lisaKasutaja() throws SQLException {
        String kasutajanimi;
        String parool;
        String kontonumber;
        double kontojääk;
        boolean onAdmin;

        boolean korrasSisestus;
        String error = "";

        do {
            korrasSisestus = true;
            System.out.println(error);
            error = "";
            System.out.println("----- Uue kasutaja lisamine ----");
            System.out.print("Sisesta kasutajanimi -> ");
            kasutajanimi = scanner.next();

            if (kasutajanimi.length() < 5) {
                error += "Error: Kasutajanimi peab olema pikem kui 5 sümbolit \n";
                korrasSisestus = false;
            }

            System.out.print("Sisesta parool -> ");
            parool = scanner.next();

            if (parool.length() < 6) {
                error += "Error: Parool peab olema pikem kui 6 sümbolit \n";
                korrasSisestus = false;
            }

            if (andmebaas.testLogimine("select * from kontod where kasutajanimi = '" + kasutajanimi + "'", kasutajanimi, "kasutajanimi")) {
                error += "Error: Sellise nimega kasutaja on juba olemas \n";
                korrasSisestus = false;
            }

            System.out.print("Sisesta kontonumber -> ");
            kontonumber = scanner.next();

            if (kontonumber.length() < 11) {
                error += "Error: Vigane kontonumber \n";
                korrasSisestus = false;
            }

            if (andmebaas.testLogimine("select * from kontod where kontonumber = '" + kontonumber + "'", kontonumber, "kontonumber")) {
                error += "Error: See kontonumber on juba kasutusel \n";
                korrasSisestus = false;
            }

            System.out.print("Sisesta kontojääk -> ");
            kontojääk = scanner.nextDouble();

            if (kontojääk < 0) {
                error+= "Error: Kontojääk on negatiivne\n";
                korrasSisestus = false;
            }


            System.out.println();
            System.out.println("(1) On administraator");
            System.out.println("(2) Ei ole administraator");

        } while (!korrasSisestus);


        System.out.println("Kasutaja lisamine õnnestus!");

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

    // Teisele kontole ülekande tegemine.
    private static void ülekanne(Kasutaja kasutaja) {
        String kontoNr;
        double summa;
        do {
            System.out.print("Sisestage millisele kontonumbrile ülekanne teha -> ");
            kontoNr = scanner.next();

            if (kontoNr.equals(kasutaja.getKontoNr())) {
                System.out.println("Iseenda kontole ei ole võimalik ülekannet teha.");
            }
        } while (kontoNr.equals(kasutaja.getKontoNr())); // Küsi uuesti kui on antud iseenda kontonumber.

        do {
            System.out.print("Sisestage summa -> ");
            summa = scanner.nextDouble();
            if (summa > kasutaja.getKontojääk()) {
                System.out.println("Teie kontol puuduvad piisavad vahendid.");
            }

        } while (summa > kasutaja.getKontojääk()); // Küsi uuesti kui summa on suurem kui kliendil raha on.

        // SQL käsklus, mis uuendab saajakonto rahalist seisu andmebaasis.
        String query = String.format("UPDATE kontod SET kontojääk = kontojääk + %s WHERE " +
                "kontonumber = '%s'", summa, kontoNr);

        // Proovitakse käsklust andmebaasi sisestada, kui kõik läheb plaanipäraselt, siis andmebaasis muutub
        // saaja konto rahaline seisund ning kasutajalt lahutatakse vastav summa maha.
        try {
            String query2 = String.format("SELECT * from kontod WHERE kontonumber = '%s'", kontoNr);
            if (andmebaas.testLogimine(query2, kontoNr, "kontonumber") == true) {
                andmebaas.sisestaBaasi(query);
                kasutaja.setKontojääk(kasutaja.getKontojääk() - summa);
                System.out.println("Ülekanne õnnestus!");
            } else {
                System.out.println("Sobimatu pangakonto number...Ülekanne ebaõnnestus...");
            } // Sellega kontrollib, kas kontonumber on olemas, erind kui ei ole.


        } catch (SQLException e) {
            System.out.println("Ülekanne ebaõnnestus...");
        }
    }

}




