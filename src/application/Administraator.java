package application;

public class Administraator extends Kasutaja {

    public Administraator(String kasutajanimi, String parool, String kontoNr, double kontojääk) {
        super(kasutajanimi, parool, kontoNr, kontojääk);
    }

    @Override
    public String toString() {
        return "Administraator " + super.toString();
    }
}
