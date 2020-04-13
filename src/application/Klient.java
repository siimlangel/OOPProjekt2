package application;

public class Klient extends Kasutaja {

    public Klient(String kasutajanimi, String parool, String kontoNr, double kontojääk) {
        super(kasutajanimi, parool, kontoNr, kontojääk);
    }

    @Override
    public String toString() {
        return "Klient " + super.toString();
    }
}
