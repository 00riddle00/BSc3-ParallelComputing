/*
 * Main.java
 * @author Tomas Giedraitis, MIF INFO 3k.
 * Lab. darbas nr. 0/1
 *
 * This was an attempt to be a valid LAB1 work,
 * but it did not meet some of the requirements 
 * for LAB1
 *
 * ====================
 * Užduoties scenarijus
 * ====================
 * Du žvejai atvažiuoja prie ežero, sėda į valtį ir nuplaukia iki jų
 * pamėgtos vietos - ne per toliausiai nuo kranto tarp vandens augalų,
 * ištįsusių per kelis metrus nuo vandens paviršiaus, yra keletas 
 * nedidelio ploto praretintų 6x6 metro vandens langelių, iš visų pusių 
 * apsuptų augmenijos. Tai - mėgstama žuvų maitinimosi vieta. Išsitraukę
 * meškeres, uždėję masalą žvejai tylomis užmeta meškeres, taikydami kuo
 * arčiau augalų, taigi apie 6 metrus nuo valties, jų plūdės išsidėsto
 * viena netoli kitos. Esant kibimui, arba norint tiesiog patikrinti 
 * masalo būklę ant kabliuko, reikia suvynioti valą. Tačiau kadangi
 * žvejybos plotelis yra itin ankštas, ir taip pat plūdės dar papildomai
 * veikiamos vandens srovės ir vėjo, valai dažnai susipainioja, jeigu
 * abu žvejai vynioja savo meškeres vienu metu. Kai valas pradedamas vynioti,
 * plūdė artėja link valties.  Jei ir kito žvejo vyniojomas valas priartina
 * ir jo plūdę, tuomet, abiem plūdėms esant 50 cm ar mažiau vienai nuo kitos,
 * valai susipainioja. Pastebėtina, kad kol valai nepradėti vynioti, o žvejojama
 * ten, kur ir buvo užmesta, laikome, kad valai negali susipainioti, nors ir 
 * atstumas tuo metu tarp plūdžių yra 0. Jei tik viena meškerė vyniojama,
 * valas susipainioti vėlgi negali. 
 *
 * Pakartotinai užmetinėjant meškeres žvejybos metu ir jas suvyniojant atgal
 * iškyla nemažai rizikingų situacijų, kuomet abi vienu metu artinamos plūdės
 * atsiranda arti viena kitos, o jeigu esant ne daugiau nei 50 cm viena 
 * kitos atžvilgiu, tai ir susipainioja valai, o žvejyba laikinai sustoja.
 * Tokiu atveju abu žvejai turi nutraukti savo valus ir perrišti meškeres. 
 * Po to žvejyba vėl tęsiasi.
 *
 * Kritinė sekcija šiuo atveju bendrai yra visas vandens plotas, kuomet abi meškerės
 * yra suvyniojamos, ir abi plūdės artėja. Siekiant išvengti meškerių perrišinėjimo
 * ir labiau pasimėgauti pačia žvejyba, žvejai sutarė, kad, nepaisant ar yra matomas
 * kibimas, jei vienas žvejas pradėjo vynioti savo valą, kitas turėtų palaukti iki
 * tol, kol pirmojo plūdė atsidurs valtyje. Tik taip susitarę jie gali tikėtis 
 * neišbaidyti žuvų ir sulaukti gero laimikio.
 *
 * =============================
 * Detalesnis programos veikimas
 * =============================
 * Uzmetus meskere, atstumas tarp valties ir pludes yra 6 m (600 cm).
 *
 * Tuomet laukiama kibimo. Po kurio laiko, su laimikiu ar be, meskere susukama.
 *
 * Meskeres susukimas vyksta iteracijomis, po vienos iteracijos plude priarteja
 * prie valties per 50 cm.
 *
 * Jei valas istraukiamas sekmingai, neuzilgo meskere permetama.
 *
 * Jei valas susipainiojo, abiems zvejams tenka istraukti susipainiojusi vala,
 * nukirpti ji ir perristi. Po to meskeres vel uzmetamos.
 *
 * Meskeres uzmetimas, kibimo laukimas, meskeres susukimas, valo perrisimas - 
 * - sie veiksmai uzima tam tikra skirtinga nedidele laiko dali, kuri realizuota 
 *   Thread.sleep() metodu, parenkant atsitiktini skaiciu kaip parametra, taciau
 *   nevirsijanti 50 milisekundziu.
 *
 * Laikome, kad zvejai (klase "Zvejas") dalyvauja kiekvienas savo zvejyboje 
 * atskirai (klase "Zvejyba"). Sios dvi zvejybos - tai atskiros gijos, 
 * prasidedancios iskvietus statini "Zvejyba" klases metoda "pradeti".
 *
 * Kiekviena gija vykdo savo metoda "zvejoti" tam tikra (taciau vienoda abejoms)
 * parinkta skaiciu iteraciju, ir pabaigoje ivykdo "baigtiZvejyba" metoda.
 *
 * Bendras zvejybos langelis - tai objektas, kuriuo dalinasi abu zvejai.
 * Jis kaupia informacija apie tai, kiek pludziu yra vandenyje, ar jos pakeliui
 * link valties, taip pat, kokio ilgio yra kiekvieno zvejo valas nuo valties 
 * iki pludes. Meskeriojant sie duomenys keiciami, taciau vyniojant vala
 * vykdomas sio objekto metodas "vyniojamasValas", kurio vykdymo metu keiciasi
 * atstumas tarp dvieju pludziu. Jei nekontroliuoti zveju prieigos prie sio
 * metodo, galima konfliktine situacija, kai abu zvejai supainioja vala
 * bevyniodami ji. Tokia situacija galima, kaip jau mineta, kai tarp pludziu
 * atstumas yra 50cm arba mazesnis. Siekiant isvengti to, prie sio metodo
 * pridedamas "synchronized" raktažodis, ir tuomet vynioti valą iki pat valties
 * gali tik vienas iš žvejų vienu metu.
 *
 * Java programą kviečiant su parametru 0 (arba be argumentų), konfliktinės
 * situacijos nebus varžomos. Tuo tarpu su parametru 1 žvejyba bus 
 * sinchronizuota.
 */

package lab0;

import java.util.Random;

public class Main {
    /**
     * @param sync : boolean - ar sinchronizuoti žvejybas
     */

    public static void main(String[] args) {
        System.out.println("Zvejyba prasideda");
        Zvejyba.pradeti();
        System.out.println("Zvejyba pasibaigia.");
    }
}

class Wait {

    public static void wait_for(int millis) {
        Random rand = new Random();
        int rand_int = rand.nextInt(millis);

        try {
            Thread.sleep(rand_int);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}

/*
   Gijos klasė, turi būti išvesta iš Thread
*/
class Zvejyba extends Thread {
    // Gijos objekto specifiniai duomenys
    BendrasZvejybosLangelis bendrasLangelis;
    Zvejas zvejas;

    // Konstruktorius, skirtas perduoti duomenis gijos objektui
    public Zvejyba(Zvejas zvejas, BendrasZvejybosLangelis bendrasLangelis) {
        this.zvejas = zvejas;
        this.bendrasLangelis = bendrasLangelis;
    }

    // Metodas, vykdomas paleidus giją
    // Thread.run()
    public void run() {
        System.out.println("Zvejo " + zvejas.zvejoNumeris + " zvejyba (gija \"" + this + ")\" prasideda");

        // Ciklas (didesnis iteracijų skaičius padidina
        // konflikto kritinėje sekcijoje galimybę
        for (int i = 0; i < 5; i++) {
            zvejas.zvejoti();
            //System.out.println("DEBUG: i=" + i);
        }
        zvejas.baigtiZvejyba();
        System.out.println("Zvejo " + zvejas.zvejoNumeris + " zvejyba (gija \"" + this + ")\" pasibaigia");
    }

    // Metodas, paleidžiantis gijas darbui ir išvedantis rezultatą
    public static void pradeti() {
        // Sukuriame objektą, kurį bendrai naudos kelios gijos     
        BendrasZvejybosLangelis bendrasLangelis = new BendrasZvejybosLangelis();
        Zvejas zvejas1 = new Zvejas(bendrasLangelis);
        Zvejas zvejas2 = new Zvejas(bendrasLangelis);

        try {
            // Sukuriame ir startuojame pirmąją giją
            // perduodame kaip parametrą objektą "bendrasLangelis"
            Thread t1 = new Zvejyba(zvejas1, bendrasLangelis);
            t1.start();

            // Sukuriame ir startuojame antraja giją
            Thread t2 = new Zvejyba(zvejas2, bendrasLangelis);
            t2.start();

            // Laukiame, kol abi gijos baigs darbą 
            t1.join();
            t2.join();

            // Išvedame galutinį rezultatą
            System.out.println("Valai neturejo buti supainioti ne karto. " +
                "Kiek kartu valai buvo supainioti: " +
                bendrasLangelis.kiekKartuSupainiota);
        } catch (InterruptedException exc) {
            System.out.println("Ivyko klaida " + exc);
        }
    }
}

/*
Klasė, aprašanti bendrai gijų naudojamą objektą
*/
class BendrasZvejybosLangelis {
    // Laukai, kuriuos modifikuos kelios gijos
    // Bus inicializuoti nuliais
    int uzmestosPludes;
    int kiekPludziuVandenyje;

    // Laukas, kurį skaitys ir modifikuos kelios gijos
    // Atstumas tarp dviejų plūdžių
    // Bus inicializuota nuliais
    int atstumasTarpPludziu;

    // Kiekviena gija skaitys ir modifikuos tik
    // jai skirta masyvo elementa
    // Masyvas bus inicializuotas nuliais
    int[] valoIlgiai = new int[2];

    // Bus inicializuoti nuliais
    public int supainiota;
    public int kiekKartuSupainiota;

    public void uzmestaPlude(int zvejoNr) {
        uzmestosPludes++;
        kiekPludziuVandenyje++;
        valoIlgiai[zvejoNr - 1] = 600;
    }

    public void isimtaPlude(int zvejoNr) {
        supainiota = Math.max(0, supainiota - 1);
        kiekPludziuVandenyje--;
    }

    // Metodas, modifikuojantis objekto turinį
    // grazina "true", jei sekmingai suvyniotas valas,
    // ir "false", jei valas uzsipainiojo betraukiant
    //
    // sinchronizuotas veikimas
    //public synchronized boolean vyniojamasValas(int zvejoNr) {
    //
    // nesinchronizuotas veikimas
    public boolean vyniojamasValas(int zvejoNr) {
        int zIndeksas = zvejoNr - 1;

        System.out.println("Zvejas[" + zvejoNr + "]: vynioja vala...");
        valoIlgiai[zIndeksas] -= 50;
        System.out.println("Zvejas[" + zvejoNr + "]: valo ilgis = " + valoIlgiai[zIndeksas] + " cm");

        uzmestosPludes--;

        while (valoIlgiai[zIndeksas] != 0) {
            // protarpiais vis patikriname, ar kartais jau
            // nėra susipainiojimo dėl kito žvejo veiklos
            if (supainiota > 0) {
                return false;
            }

            System.out.println("Zvejas[" + zvejoNr + "]: vynioja vala...");

            valoIlgiai[zIndeksas] -= 50;

            System.out.println("Zvejas[" + zvejoNr + "]: valo ilgis = " + valoIlgiai[zIndeksas] + " cm");

            atstumasTarpPludziu = Math.abs(valoIlgiai[0] - valoIlgiai[1]);
            System.out.println("Zvejas[" + zvejoNr + "]: Atstumas tarp pludziu = " + atstumasTarpPludziu + " cm");

            if (atstumasTarpPludziu <= 50) {
                if (kiekPludziuVandenyje == 2 && uzmestosPludes == 0) {

                    if (supainiota > 0) {
                        return false;
                    }

                    System.out.println("=============== Supainiota! ==================");
                    supainiota += 2;
                    kiekKartuSupainiota++;
                    return false;
                }
            }

            if (supainiota > 0) {
                return false;
            }

            Wait.wait_for(20);

            if (supainiota > 0) {
                return false;
            }
        }

        return true;
    }
}

/*
Klasė, aprašanti bendrai gijų naudojamą objektą
*/
class Zvejas {
    public BendrasZvejybosLangelis zvejybosLangelis;

    private boolean pludeUzmesta;
    private boolean pludeValtyje;

    public int zvejoNumeris;

    static int zvejuSkaicius = 0;

    public Zvejas(BendrasZvejybosLangelis bendrasLangelis) {
        pludeUzmesta = false;
        pludeValtyje = true;
        zvejybosLangelis = bendrasLangelis;
        zvejuSkaicius++;
        zvejoNumeris = zvejuSkaicius;
    }

    public void baigtiZvejyba() {
        if (!pludeValtyje) {
            // bus susukamas valas paskutini karta
            zvejoti();
        }
    }

    public void zvejoti() {
        if (pludeValtyje) {
            uzmestiPlude();
        } else {
            if (pludeUzmesta) {
                System.out.println("Zvejas[" + zvejoNumeris + "]: laukia kibimo...");

                Wait.wait_for(30);
            }

            boolean valasSuvyniotasSekmingai = vyniotiVala();
            isimtiPlude();

            if (!valasSuvyniotasSekmingai) {
                perristiVala();
            }
        }
    }

    public void uzmestiPlude() {
        System.out.println("Zvejas[" + zvejoNumeris + "]: uzmeta plude...");
        Wait.wait_for(50);

        pludeValtyje = false;
        pludeUzmesta = true;
        zvejybosLangelis.uzmestaPlude(zvejoNumeris);
    }

    public void perristiVala() {
        System.out.println("Zvejas[" + zvejoNumeris + "]: perrisineja vala...");
        Wait.wait_for(100);
    }

    public void isimtiPlude() {
        System.out.println("Zvejas[" + zvejoNumeris + "]: isima plude...");
        Wait.wait_for(50);

        pludeUzmesta = false;
        pludeValtyje = true;
        zvejybosLangelis.isimtaPlude(zvejoNumeris);
    }

    public boolean vyniotiVala() {
        if (pludeUzmesta) {
            pludeUzmesta = false;
        }

        Wait.wait_for(50);

        return zvejybosLangelis.vyniojamasValas(zvejoNumeris);
    }
}
