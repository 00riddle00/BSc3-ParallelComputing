/*
 * Main.java
 * @author Tomas Giedraitis, MIF INFO 3k.
 * Lab. darbas nr. 3
 *
 * Užduotis:
 *
 * Automato "Conway's Life Game" simuliacija.
 * Pageidautina rezultatą pavaizduoti vizualiai.
 *
 * Suprojektuoti pateiktajai užduočiai (problemai) efektyvų lygiagretaus vykdymo
 * algoritmą, jį realizuoti JAVA gijomis ir ištirti sprendimo efektyvumą.

 * "Darbinių" gijų skaicius - programos parametras. Jos "paleidžiamos" vykdymui programos
 * pradžioje ir funkcionuoja iki pat programos pabaigos (kol užduotyje specifikuotas
 * darbas nebus atliktas).

 * Sąveika tarp atskirų gijų turi būti tokia, kad jos būtų maksimaliai "apkrautos". Be to
 * NELEISTINA tokia "darbo" vykdymo disciplina, kai "darbo" dalys paskirstomos atskiriems
 * "procesoriams" - gijoms iš anksto ir nekinta programos vykdymo eigoje (kitaip tariant,
 * neleistinas sinchronizacijos tarp atskirų gijų nebuvimas)

 * Paruošti programos vykdymą dviem režimais - derinimo režimu, kuriame programos vykdymas
 * gali būti dirbtinai sulėtintas (sleep) ir į ekraną išvedamas programos vykdymo
 * protokolas, paaiškinantis veikimo principus, bei "sparčiuoju" režimu, kuriame neturi
 * būti dirbtinių stabdymų, o programa išvestų į ekraną vykdymo trukmę. Komandinė eilutė
 * turi turėti papildomus parametrus - darbinių gijų skaičių bei "apkrovos" parametrą,
 * nusakantį darbo apimtį (pvz., tikslumas, masyvo dydis ir t.t. ).

 * Sparčiuoju režimu programa turi demonstruoti vykdymo spartinimą, vykdant ją
 * daugiaprocesorinėje sistemoje (http://kedras.mif.vu.lt/cluster/).

 * Sistemoje, kurioje testuosite savo programą, būtina įvykdyti programą TTest, kuri
 * leidžia patikrinti java programos vykdymo spartinimą, ir pateikti jos darbo rezultatus:

 * Eksperimentiškai nustatyti algoritmo spartinimo, plečiamumo (scaling) bei vykd. laiko
 * priklausomybės nuo "darbų" dydžio (grain size) charakteristikas. Sugebėti pagrįsti
 * gautuosius rezultaþNtus. Atsiskaitant, pateikti (motyvuotą) sprendimo aprašą, įtraukiant
 * minetųjų priklausomybių grafikus
 *
 */

//package lab3.parallel_no_print;

import java.util.Random;

public class Main {

    public static void main(String[] args) {

        int rows = 200;
        int cols = 200;
        int iterCount = 300000;
        int stage = 0;
        int threadCount = 20;
        
        GameOfLife game = new GameOfLife(rows, cols, iterCount);
        game.initStage(stage);

        //game.printState(false);
        
        long start = System.currentTimeMillis();

        Barrier barrier = new Barrier(game, threadCount);

        //Worker w1 = new Worker(1, barrier, game, 0, 1);
        //Worker w2 = new Worker(2, barrier, game, 2, 3);
        //Worker w3 = new Worker(3, barrier, game, 4, 5);
        //Worker w4 = new Worker(4, barrier, game, 6, 7);
        //Worker w5 = new Worker(5, barrier, game, 8, 9);

        //Worker w1 = new Worker(1, barrier, game, 0, 19);
        //Worker w2 = new Worker(2, barrier, game, 20, 39);
        //Worker w3 = new Worker(3, barrier, game, 40, 59);
        //Worker w4 = new Worker(4, barrier, game, 60, 79);
        //Worker w5 = new Worker(5, barrier, game, 80, 99);

        Worker w1 = new Worker(1, barrier, game, 0, 9);
        Worker w2 = new Worker(2, barrier, game, 10, 19);
        Worker w3 = new Worker(3, barrier, game, 20, 29);
        Worker w4 = new Worker(4, barrier, game, 30, 39);
        Worker w5 = new Worker(5, barrier, game, 40, 49);
        Worker w6 = new Worker(6, barrier, game, 50, 59);
        Worker w7 = new Worker(7, barrier, game, 60, 69);
        Worker w8 = new Worker(8, barrier, game, 70, 79);
        Worker w9 = new Worker(9, barrier, game, 80, 89);
        Worker w10 = new Worker(10, barrier, game, 90, 99);

        Worker w11 = new Worker(11, barrier, game, 100, 109);
        Worker w12 = new Worker(12, barrier, game, 110, 119);
        Worker w13 = new Worker(13, barrier, game, 120, 129);
        Worker w14 = new Worker(14, barrier, game, 130, 139);
        Worker w15 = new Worker(15, barrier, game, 140, 149);
        Worker w16 = new Worker(16, barrier, game, 150, 159);
        Worker w17 = new Worker(17, barrier, game, 160, 169);
        Worker w18 = new Worker(18, barrier, game, 170, 179);
        Worker w19 = new Worker(19, barrier, game, 180, 189);
        Worker w20 = new Worker(20, barrier, game, 190, 199);

        //w1.start(); 
        //w2.start();
        //w3.start();
        //w4.start();
        //w5.start();

        w1.start(); 
        w2.start();
        w3.start();
        w4.start();
        w5.start();
        w6.start();
        w7.start();
        w8.start();
        w9.start();
        w10.start();
        w11.start();
        w12.start();
        w13.start();
        w14.start();
        w15.start();
        w16.start();
        w17.start();
        w18.start();
        w19.start();
        w20.start();

        try {
            //w1.join(); 
            //w2.join(); 
            //w3.join();
            //w4.join();
            //w5.join();

            w1.join(); 
            w2.join(); 
            w3.join();
            w4.join();
            w5.join();
            w6.join();
            w7.join();
            w8.join();
            w9.join();
            w10.join();
            w11.join();
            w12.join();
            w13.join();
            w14.join();
            w15.join();
            w16.join();
            w17.join();
            w18.join();
            w19.join();
            w20.join();
            
        } catch (InterruptedException exc) {
            exc.printStackTrace();
        }

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;

        game.printState(false);

        System.out.println("Time: " + timeElapsed + "ms");
    }
}

class Barrier {
    
    private int threads = 0;
    private int expectedThreads;

    private GameOfLife game;
    
    public Barrier(GameOfLife game, int expectedThreads) {
        this.game = game;
        this.expectedThreads = expectedThreads;
    }

    public synchronized void waitBarrier(int stage) throws InterruptedException {
        //1. caller connects and we add his value
        threads++;
        
        //2. now wait for everyone to connect
        if (threads != expectedThreads) {
            wait();
        } else { // 3. IF everyone is connected
            if (stage == 1) {
                //game.printNeighbourGrid();
            } else if (stage == 2) {
                game.updateGrid();
                //game.printState(false);
            }

            //tell everyone
            notifyAll();
            
            //reset the thread count
            threads = 0;
        }
        return;
    }
}

class Worker extends Thread {

    private int threadNo;
    private int firstRow;
    private int lastRow;
    private Barrier barrier;

    private GameOfLife game;

    public Worker(int threadNo, Barrier barrier, GameOfLife game, int firstRow, int lastRow) {
        this.threadNo = threadNo;
        this.barrier = barrier;
        this.game = game;
        this.firstRow = firstRow;
        this.lastRow = lastRow;
    }

    public void run() {
        try {
            for (int i = 1; i <= game.iterCount; i++) {
                //Thread.sleep(1000);

                game.updateNeighbourCount(firstRow, lastRow);
                //barrier.waitBarrier(1);

                game.updateCells(firstRow, lastRow);
                barrier.waitBarrier(2);
            }

        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class GameOfLife {
    private int rows;
    private int cols;

    private int grid[][];
    private int neighbourGrid[][];
    private int newGrid[][];
    public int iterCount;

    public GameOfLife(int rows, int cols, int iterCount) {
        this.rows = rows;
        this.cols = cols;
        this.iterCount = iterCount;
        this.grid = new int[rows][cols];
        this.newGrid = new int[rows][cols];
        this.neighbourGrid = new int[rows][cols];
    }

    private int[][] deepCopy(int[][] A) {
        int[][] B = new int[A.length][A[0].length];
        for (int x = 0; x < A.length; x++) {
            for (int y = 0; y < A[0].length; y++) {
                if (A[x][y] == 1) { //write only when necessary
                    B[x][y] = A[x][y];
                }
            }
        }
        return B;
    }

    public void updateGrid() {
        grid = deepCopy(newGrid);
        newGrid = new int[rows][cols];
    }

    public void updateNeighbourCount(int firstRow, int lastRow) {
        for (int i = firstRow; i <= lastRow; i++) {
            for (int j = 0; j < cols; j++) {
                updateCellNeighbourCount(i, j);
            }
        }
    }

    private void updateCellNeighbourCount(int i, int j) {
        // i = row, j = column

        // neighbour cell count
        int ncc = 0;

        if ((j-1) >= 0)
            ncc += grid[i][j-1];

        if ((j+1) < cols)
            ncc += grid[i][j+1];

        if ((i-1) >= 0) {
            ncc += grid[i-1][j];

            if ((j-1) >= 0) 
                ncc += grid[i-1][j-1];

            if ((j+1) < cols)
                ncc += grid[i-1][j+1];
        }

        if ((i+1) < rows) {
            ncc += grid[i+1][j];
            if ((j-1) >= 0)
                ncc += grid[i+1][j-1];

            if ((j+1) < cols)
                ncc += grid[i+1][j+1];
        }

        neighbourGrid[i][j] = ncc;
    }

    public void updateCells(int firstRow, int lastRow) {
        for (int i = firstRow; i <= lastRow; i++) {
            for (int j = 0; j < cols; j++) {
                int ncc = neighbourGrid[i][j];

                if (ncc < 2 || ncc > 3) {
                    newGrid[i][j] = 0;
                    //grid[i][j] = 0;
                } else if (ncc == 2) {
                    if (grid[i][j] == 1)
                        newGrid[i][j] = 1;
                        //grid[i][j] = 1;
                } else if (ncc == 3) {
                    newGrid[i][j] = 1;
                    //grid[i][j] = 1;
                }
            }
        }
    }

    public void printNeighbourGrid() {
        for (int i = 0; i < cols; i++) {
            System.out.print("._");
        }
        System.out.println(".");

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                System.out.print("|");
                System.out.print(neighbourGrid[i][j]);
            }
            System.out.println("|");
        }
    }

    public void printState(boolean clearScreen) {
        if (clearScreen) {
            System.out.println("\033[2J\033[H");
        }

        for (int i = 0; i < cols; i++) {
            System.out.print("._");
        }
        System.out.println(".");

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                if (grid[i][j] == 1) {
                    System.out.print("|O");
                } else {
                    System.out.print("|_");
                }
            }
            System.out.println("|");
        }
    }

    private void validateGrid(int dimension) {
        if (rows < dimension || cols < dimension) {
            System.out.println(
                    "ERROR: The number of rows and/or "
                    + "columns is too small for this stage");
            System.exit(1);
        }
    }

    public void initStage(int stage) {
        switch (stage) {
        case 0:       // Random stage
            Random rand = new Random();
            rand.setSeed(0);

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    grid[i][j] = rand.nextInt(2);
                }
            }
            break;
        case 1:       // Block (period = 1, stays still)
            validateGrid(4);
            // --------------
            grid[1][1] = 1;
            grid[1][2] = 1;
            grid[2][1] = 1;
            grid[2][2] = 1;
            break;
        case 2:       // Blinker (period = 2, stays still)
            validateGrid(5);
            // --------------
            grid[1][2] = 1;
            grid[2][2] = 1;
            grid[3][2] = 1;
            break;
        case 3:       // Toad (period = 2, stays still)
            validateGrid(6);
            // --------------
            grid[1][3] = 1;
            grid[2][1] = 1;
            grid[2][4] = 1;
            grid[3][1] = 1;
            grid[3][4] = 1;
            grid[4][2] = 1;
            break;
        case 4:       // Pulsar (period = 3, stays still)
            validateGrid(17);
            // --------------
            grid[2][4] =   1;
            grid[2][5] =   1;
            grid[2][6] =   1;
            grid[2][10] =  1;
            grid[2][11] =  1;
            grid[2][12] =  1;
            // --------------
            grid[7][4] =   1;
            grid[7][5] =   1;
            grid[7][6] =   1;
            grid[7][10] =  1;
            grid[7][11] =  1;
            grid[7][12] =  1;
            // --------------
            grid[9][4] =   1;
            grid[9][5] =   1;
            grid[9][6] =   1;
            grid[9][10] =  1;
            grid[9][11] =  1;
            grid[9][12] =  1;
            // --------------
            grid[14][4] =  1;
            grid[14][5] =  1;
            grid[14][6] =  1;
            grid[14][10] = 1;
            grid[14][11] = 1;
            grid[14][12] = 1;
            // --------------

            // --------------
            grid[4][2] =   1;
            grid[4][7] =   1;
            grid[4][9] =   1;
            grid[4][14] =  1;
            // --------------
            grid[5][2] =   1;
            grid[5][7] =   1;
            grid[5][9] =   1;
            grid[5][14] =  1;
            // --------------
            grid[6][2] =   1;
            grid[6][7] =   1;
            grid[6][9] =   1;
            grid[6][14] =  1;
            // --------------
            grid[10][2] =  1;
            grid[10][7] =  1;
            grid[10][9] =  1;
            grid[10][14] = 1;
            // --------------
            grid[11][2] =  1;
            grid[11][7] =  1;
            grid[11][9] =  1;
            grid[11][14] = 1;
            // --------------
            grid[12][2] =  1;
            grid[12][7] =  1;
            grid[12][9] =  1;
            grid[12][14] = 1;
            // --------------
            break;
        case 5:       // Glider (period = 4, moves southeast)
            validateGrid(4);
            // --------------
            grid[0][2] = 1;
            grid[1][0] = 1;
            grid[1][2] = 1;
            grid[2][1] = 1;
            grid[2][2] = 1;
            break;
        case 6:       // Middle-weight spaceship (MWSS) (period = 4, moves east)
            validateGrid(10);
            // --------------
            grid[3][5] = 1;
            grid[3][6] = 1;
            // --------------
            grid[4][2] = 1;
            grid[4][3] = 1;
            grid[4][4] = 1;
            grid[4][6] = 1;
            grid[4][7] = 1;
            // --------------
            grid[5][2] = 1;
            grid[5][3] = 1;
            grid[5][4] = 1;
            grid[5][5] = 1;
            grid[5][6] = 1;
            // --------------
            grid[6][3] = 1;
            grid[6][4] = 1;
            grid[6][5] = 1;
            break;
        default:
            System.out.println("ERROR: Unsupported initial stage");
            System.exit(1);
        }
    }
}

