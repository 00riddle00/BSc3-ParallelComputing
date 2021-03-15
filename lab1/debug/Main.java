/*
 * Main.java
 * @author Tomas Giedraitis, MIF INFO 3k.
 * Lab. darbas nr. 1
 *
 * Programa vykdo zveju pludziu atsitiktini pozicijos keitima (3 zvejai).
 * Zvejybos plotas: 5x5, t.y. sarasas [ [0,0], ... [0,4] ]
 *                                    [ [1,0], ... [1,4] ]
 *                                            ...
 *                                    [ [4,0], ... [4,4] ]
 *
 * Pradines pozicijos: (0,4), (2,2), (4,4).
 *
 * Kiekvienas zvejys atsitiktiniu budu pakeicia pludes x ir y pozicija 
 * per 1 langeli, arba palieka plude stoveti vietoje.
 *
 * Galutinis rezultatas, nesvarbu kiek iteraciju vykdoma, turetu buti 
 * sekminga zvejyba be pludziu konflikto tame paciame langelyje (supainiota = 0).
 * 
 * Tam vykdoma nepertraukiama tranzakcija:
 *  1. Patikrinti, ar langelis laisvas
 *  2. Nuvesti plude to langelio link, jei laisvas
 *
 *  Tai galioja tik tuomet, kada pludes padetis yra keiciama, 
 *  priesingu atveju tikrinimas nevykdomas.
 *
 *  Neapsaugojus sios tranzakcijos priemone synchronized,
 *  galimos konfliktines situacijos, kuomet pludes susipainioja,
 *  patekusios i ta pati langeli. Tuomet rezultatas - konfliktiniu 
 *  situaciju skaicius, didesnis uz nuli (supainiota > 0).
 *
 */

package lab1.debug;

public class Main {
    public static void main(String[] args) {
        System.out.println("--------------------");
        System.out.println("Žvejyba prasideda");
        System.out.println("--------------------");
        Zvejyba.pradeti();
        System.out.println("Žvejyba pasibaigia.");
    }
}

class Plude {
    int xPos;
    int yPos;
    int xNew;
    int yNew;
    boolean needToMove;
    ZvejybosPlotas bendrasPlotas;

    public int no;

    public static int total_no = 1;

    public Plude(int xPos, int yPos, ZvejybosPlotas bendrasPlotas) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.bendrasPlotas = bendrasPlotas;
        bendrasPlotas.uzimta[xPos][yPos] = true;
        this.no = total_no;
        total_no++;
        System.out.println("[plūdė" + no + "(p" + no + ")] [pradinė pozicija]: (" + xPos + "," + yPos + ")");
        System.out.println("--------------------");
    }

    public void chooseNewPos(int delta_x, int delta_y) {
        System.out.println("[p" + no + "] [prieš pasirenkant]: (" + xPos + "," + yPos + ") + (" + delta_x + "," + delta_y + ")");
        xNew = xPos + delta_x;
        yNew = yPos + delta_y;

        if (xNew < 0) xNew = 0;
        if (yNew < 0) yNew = 0;
        if (xNew > bendrasPlotas.xMax) xNew = bendrasPlotas.xMax;
        if (yNew > bendrasPlotas.yMax) yNew = bendrasPlotas.yMax;

        System.out.println("[p" + no + "] [po pasirinkimo]:    (" + xNew + "," + yNew + ")");

        if (xNew != xPos || yNew != yPos) {
            System.out.println("[p" + no + "] [bus perkelta]");
            needToMove = true; 
        } else {
            System.out.println("[p" + no + "] [nebus perkelta]");
            needToMove = false;
        }
    }
    
    public boolean needToMove() {
        return needToMove;
    }

    public boolean checkIsNewPosFree() {
        if (!bendrasPlotas.uzimta[xNew][yNew]) return true; else return false;
    }

    public void changePos() {
        bendrasPlotas.uzimta[xPos][yPos] = false;
        this.xPos = this.xNew;
        this.yPos = this.yNew;
        System.out.println("[p" + no + "] [nauja pozicija]:    (" + this.xPos + "," + this.yPos + ")");
        System.out.println("--------------------");
        if (bendrasPlotas.uzimta[xPos][yPos]) {
            bendrasPlotas.supainiota++;
            System.out.println("==================== Supainiota! =====================");
        }
        bendrasPlotas.uzimta[xPos][yPos] = true;
    }
}

class ZvejybosPlotas {
    public int xMax;
    public int yMax;
    public boolean[][] uzimta;
    public int supainiota;

    public ZvejybosPlotas(int xMax, int yMax) {
        this.xMax = xMax;
        this.yMax = yMax;
        uzimta = new boolean[xMax+1][yMax+1];
    }
}

// Gijos klasė, turi būti išvesta iš Thread
class Zvejyba extends Thread {
    // Gijos objekto specifiniai duomenys
    ZvejybosPlotas bendrasPlotas;
    Plude plude;

    int iteracijos = 10000;

    // Konstruktorius, skirtas perduoti duomenis gijos objektui
    public Zvejyba(ZvejybosPlotas bendrasPlotas, Plude plude) {
        this.bendrasPlotas = bendrasPlotas;
        this.plude = plude;
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    // Metodas, vykdomas paleidus giją
    // Thread.run()
    public void run() {

        for (int i = 0; i < iteracijos; i++) {
            plude.chooseNewPos(getRandomNumber(-2,2),getRandomNumber(-2,2));

            if (plude.needToMove()) {
                //synchronized(bendrasPlotas) {
                    if (plude.checkIsNewPosFree()) {
                        plude.changePos();
                    }
                //}
            }
        }
    }

    // Metodas, paleidžiantis gijas darbui ir išvedantis rezultatą
    public static void pradeti() {
        // Sukuriame objektą, kurį bendrai naudos kelios gijos     
        ZvejybosPlotas bendrasPlotas = new ZvejybosPlotas(4,4);
        Plude p1 = new Plude(0, 4, bendrasPlotas);
        Plude p2 = new Plude(2, 2, bendrasPlotas);
        Plude p3 = new Plude(4, 4, bendrasPlotas);

        try {
            // Sukuriame gijas, perduodame kaip parametrą objektą "bendrasPlotas"
            Thread z1 = new Zvejyba(bendrasPlotas, p1);
            Thread z2 = new Zvejyba(bendrasPlotas, p2);
            Thread z3 = new Zvejyba(bendrasPlotas, p3);

            // Startuojame gijas
            z1.start(); z2.start(); z3.start();

            // Laukiame, kol gijos baigs darbą 
            z1.join(); z2.join(); z3.join();

            // Išvedame galutinį rezultatą
            System.out.println("--------------------");
            System.out.println("Supainiota: " + bendrasPlotas.supainiota + " kartų(us)");
            System.out.println("--------------------");
        } catch (InterruptedException exc) {
            System.out.println("Ivyko klaida " + exc);
        }
    }
}
