package application;

public abstract class Kasutaja {
    private String kasutajanimi;
    private String parool;
    private double kontojääk;
    private String kontoNr;

    public Kasutaja(String kasutajanimi, String parool, String kontoNr, double kontojääk) {
        this.kasutajanimi = kasutajanimi;
        this.parool = parool;
        this.kontojääk = kontojääk;
        this.kontoNr = kontoNr;
    }

    public void setParool(String parool) {
        this.parool = parool;
    }

    public String getKasutajanimi() {
        return kasutajanimi;
    }

    public String getParool() {
        return parool;
    }

    public String getKontoNr() {
        return kontoNr;
    }

    public double getKontojääk() {
        return kontojääk;
    }


    public void setKontojääk(double kontojääk) {
        this.kontojääk = kontojääk;
    }

    @Override
    public String toString() {
        return "Kasutaja{" +
                "kasutajanimi='" + kasutajanimi + '\'' +
                ", parool='" + parool + '\'' +
                ", kontojääk=" + kontojääk +
                ", kontoNr='" + kontoNr + '\'' +
                '}';
    }
}
