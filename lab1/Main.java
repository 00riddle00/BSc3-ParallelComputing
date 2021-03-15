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

package lab1;

public class Main {
    public static void main(String[] args) {
        Zvejyba.pradeti();
    }
}

class Plude {
    int xPos, yPos, xNew, yNew;
    boolean needToMove;
    ZvejybosPlotas bendrasPlotas;

    public Plude(int xPos, int yPos, ZvejybosPlotas bendrasPlotas) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.bendrasPlotas = bendrasPlotas;
        bendrasPlotas.uzimta[xPos][yPos] = true;
    }

    public void chooseNewPos(int delta_x, int delta_y) {
        xNew = xPos + delta_x;
        yNew = yPos + delta_y;

        if (xNew < 0) xNew = 0;
        if (yNew < 0) yNew = 0;
        if (xNew > bendrasPlotas.xMax) xNew = bendrasPlotas.xMax;
        if (yNew > bendrasPlotas.yMax) yNew = bendrasPlotas.yMax;

        if (xNew != xPos || yNew != yPos) needToMove = true; else needToMove = false;
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
        if (bendrasPlotas.uzimta[xPos][yPos]) {
            bendrasPlotas.supainiota++;
            System.out.println("Supainiota!");
        }
        bendrasPlotas.uzimta[xPos][yPos] = true;
    }
}

class ZvejybosPlotas {
    public int xMax, yMax;
    public boolean[][] uzimta;
    public int supainiota;

    public ZvejybosPlotas(int xMax, int yMax) {
        this.xMax = xMax;
        this.yMax = yMax;
        uzimta = new boolean[xMax+1][yMax+1];
    }
}

class Zvejyba extends Thread {
    ZvejybosPlotas bendrasPlotas;
    Plude plude;

    int iteracijos = 10000;

    public Zvejyba(ZvejybosPlotas bendrasPlotas, Plude plude) {
        this.bendrasPlotas = bendrasPlotas;
        this.plude = plude;
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

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

    public static void pradeti() {
        ZvejybosPlotas bendrasPlotas = new ZvejybosPlotas(4,4);
        Plude p1 = new Plude(0, 4, bendrasPlotas);
        Plude p2 = new Plude(2, 2, bendrasPlotas);
        Plude p3 = new Plude(4, 4, bendrasPlotas);

        try {
            Thread z1 = new Zvejyba(bendrasPlotas, p1);
            Thread z2 = new Zvejyba(bendrasPlotas, p2);
            Thread z3 = new Zvejyba(bendrasPlotas, p3);

            z1.start(); z2.start(); z3.start();

            z1.join(); z2.join(); z3.join();

            System.out.println("Supainiota: " + bendrasPlotas.supainiota + " kartų(us)");
        } catch (InterruptedException exc) {
            System.out.println("Ivyko klaida " + exc);
        }
    }
}
