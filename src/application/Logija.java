package application;

import java.util.logging.*;

class Logija {

    public final static Logger logija = Logger.getLogger( Logger.GLOBAL_LOGGER_NAME );

    static void logijaProtsess() {

        LogManager.getLogManager().reset();
        logija.setLevel(Level.ALL);

        try {
            // Append: true - lisame olemasolevasse logifaili juurde.
            FileHandler käsitleja = new FileHandler("logi.log", true);
            käsitleja.setLevel(Level.FINE);
            logija.addHandler(käsitleja);
            // Andmed sobivale kujule väljastamiseks...
            SimpleFormatter formaat = new SimpleFormatter();
            käsitleja.setFormatter(formaat);
        } catch (Exception erind) {
            logija.log(Level.WARNING, "Logimisprotsess ei tööta korrektselt...", erind);
        }

    }

}
