package application;

import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Ul1var2 {

    /**
     *
     * @param x
     * @param s
     * @return
     */

    public static int ul1 (List<Integer> x, double s){
        Collections.sort(x); //Pannakse järjendis olevad vääringud suuruse järjekorda
        Collections.reverse(x); //Järjendi elemendid pööratakse teistpidi, et järjend oleks kahanevas järjekorras.
        int summa = (int) (s *100); //Korrutatakse sajaga, kuna siis on soovitud summa ning vääringud samal skaalal.
        List<Integer> kasutatudVääringud = new ArrayList<>(); //Tühi järjend, kus vääringuid hoida.
        return ul1rek(x, summa, kasutatudVääringud, 0); //Kutsun välja rekursiivse meetodi ning tagastan selle tagastatud väärtuse.
    }

    /**
     *
     * @param x
     * @param s
     * @param kasutatudVääringud
     * @param i
     * @return
     */

    public static int ul1rek(List<Integer> x, int s, List<Integer> kasutatudVääringud, int i){
        int hetkesumma = vääringuteSumma(kasutatudVääringud); //Salvestan selle väljakutse summa muutujasse.
        if (hetkesumma == s) //Kontrollin, kas nende vääringute summa on võrdne tahetud summaga.
            return kasutatudVääringud.size(); //Tagastan järjendi suuruse, ehk kui palju vääringuid on kasutatud.

            //Kui hetkesumma on üle summa läinud, siis on vääringuid liiga palju, eemaldan viimase lisatud vääringu,
            //suurendan i väärtust, mille järgi võetakse elemente järjendist x ning lisan uue vääringu ja kutsun meetodi uuesti välja.
        else if (hetkesumma > s) {
            kasutatudVääringud.remove(kasutatudVääringud.size() - 1);

            //Siin peaks ilmselt olema mingi kontroll, et see i ei läheks üle järjendi suuruse, aga ma ei oska midagi teha, kui tingimus täidetud

            i += 1;
            kasutatudVääringud.add(x.get(i));
            return ul1rek(x, s, kasutatudVääringud, i);

        }

        //Kui kumbki if lausetest ei saanud täidetud, siis lisatakse uus vääring ning kutsutakse meetod uuesti välja.
        kasutatudVääringud.add(x.get(i));
        return ul1rek(x, s, kasutatudVääringud, i);


    }

    /**
     *
     * @param kasutatudVääringud
     * @return
     */

    public static int vääringuteSumma(List<Integer> kasutatudVääringud){ //Meetod summa leidmiseks hetkel kasutatud vääringutest.
        int summa = 0; //Muutuja, kuhu salvestatakse vääringute summa.
        for (Integer vääring : kasutatudVääringud) {
            summa += vääring;
        }
        return summa; //Tagastan vääringute summa.
    }

    public static void main(String[] args) {
        List<Integer> x = new ArrayList<>(Arrays.asList(2, 5, 10, 20));
        System.out.println(ul1(x, 0.81));
    }
}