package application;

import java.sql.*;

public class AndmebaasiUhendaja {
    private Connection ühendus;
    private Statement st;
    private ResultSet loeAndmeid;
    private int sisestaAndmeid;

    // Ühendab programmi andmebaasiga vastavate sisenditega.
    public AndmebaasiUhendaja() {
        // Serveri, andmebaasi andmed, millise andmebaasiga ühendust võtab.
        try {
            String url = "jdbc:mysql://35.228.242.85:3306/andmed?serverTimezone=UTC";
            String kasutaja = "kasutaja";
            String parool = "gXPcow123";
            ühendus = DriverManager.getConnection(url, kasutaja, parool);
            System.out.println("Andmebaas ühendatud.");
            st = ühendus.createStatement();

        } catch (SQLException erind) {
            System.out.println("Viga andmebaasi ühendamisel.");
            System.out.println("Erind: " + erind);
        }

    }

    // Programm loob sisselogimisprotsessis saadud andmete põhjal uue isendi klassis Kasutaja, millele loeb
    // vastavatesse sõnedesse andmebaasist vastavad väärtused, st parool, kontonumnber jne.

    public Kasutaja looKasutaja(String kasutajanimi) throws SQLException {
        String parool = "";
        String kontoNr = "";
        double kontojääk = 0;
        boolean admin = false;
        Kasutaja sisseLogitu;
        loeAndmeid = st.executeQuery("select * from kontod where kasutajanimi = '" + kasutajanimi + "'");
        while(loeAndmeid.next()) {
            parool = loeAndmeid.getString("parool");
            kontoNr = loeAndmeid.getString("kontonumber");
            kontojääk = loeAndmeid.getDouble("kontojääk");
            admin = loeAndmeid.getBoolean("admin");
        }
        if (!admin) {
            sisseLogitu = new Klient(kasutajanimi, parool, kontoNr, kontojääk);
        } else {
            sisseLogitu = new Administraator(kasutajanimi, parool, kontoNr, kontojääk);
        }
        return sisseLogitu;
    }



    // Kontrollitakse sisselogimisprotsessi käigus, kas kasutaja poolt sisestatud andmed (st kasutajanimi, parool) vastavad
    // andmetele, mis on leitavad andmebaasist.
    public boolean testLogimine(String sql, String sisend, String lahter) throws SQLException {
        loeAndmeid = st.executeQuery(sql);

        while (loeAndmeid.next()) {
            if (loeAndmeid.getString(lahter).equals(sisend)) {
                return true;
            }
        }
        return false;
    }

    // Võimaldab uued andmed salvestada andmebaasi.
    public void sisestaBaasi(String sql) throws SQLException {
        sisestaAndmeid = st.executeUpdate(sql);
    }

    // Lisab uue kasutaja andmebaasi.
    public void lisaKasutaja(String lisadaKasutajanimi, String lisadaParool, String lisadaKontoNr, double lisadaKontoJääk, boolean admin) throws SQLException {
        int onAdmin = admin ? 1 : 0; // Kui uus kasutaja on administraator ehk 'true' siis andmebaasi sisestatakse vastavasse
        // lahtrisse 1, vastasel juhul 0.
        String query = String.format("INSERT INTO kontod(kasutajanimi, parool, kontonumber, kontojääk, admin) VALUES('%s', '%s', '%s', %s, %d)",
                lisadaKasutajanimi, lisadaParool, lisadaKontoNr, lisadaKontoJääk, onAdmin);
        sisestaBaasi(query);
    }

    // Eemaldab kasutaja andmebaasist.
    public void eemaldaKasutaja(String kasutajaNimi) throws SQLException {
        String query = String.format("DELETE FROM kontod WHERE kasutajanimi = '%s'", kasutajaNimi);
        sisestaBaasi(query);
    }

    // Loeb andmebaasist, mis on etteantud kasutaja parool.
    public String getParool(String kasutajanimi) throws SQLException {
        String parool = "";
        String query = String.format("SELECT parool " +
                "FROM kontod " +
                "WHERE kasutajanimi = '%s'", kasutajanimi);
        ResultSet vastus = st.executeQuery(query);
        while (vastus.next()) {
            parool = vastus.getString("parool");
        }
        return parool;
    }

}