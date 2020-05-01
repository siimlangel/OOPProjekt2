package application;

import java.util.logging.*;

class Logija {

    // Loome globaalse logija.
    public final static Logger logija = Logger.getLogger( Logger.GLOBAL_LOGGER_NAME );

    static void logijaProtsess() {

        LogManager.getLogManager().reset();
        logija.setLevel(Level.ALL);

        try {
            // Append: true - lisame olemasolevasse logifaili juurde.
            FileHandler käsitleja = new FileHandler("logike.log", true);
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
