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

//package lab3;

import java.util.Arrays;

public class Main {

	public static void main(String[] args) {
        int rows = 5;
        int cols = 5;
        int iterCount = 5;
        int stage = 2;
        
        GameOfLife game = new GameOfLife(rows, cols);
        game.initStage(stage);

        //System.out.println("\033[2J\033[H");
        game.printState();

        for (int i = 0; i < iterCount; i++) {
            try {
                Thread.sleep(1000);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            try {
                Worker w1 = new Worker(1, game, 0, 0);
                Worker w2 = new Worker(2, game, 1, 1);
                Worker w3 = new Worker(3, game, 2, 2);
                Worker w4 = new Worker(4, game, 3, 3);
                Worker w5 = new Worker(5, game, 4, 4);
                
                w1.start(); 
                w2.start();
                w3.start();
                w4.start();
                w5.start();

                w1.join(); 
                w2.join();
                w3.join();
                w4.join();
                w5.join();

                //game.printNeighbourGrid();

                game.taskNo++;

                Worker w01 = new Worker(6, game, 0, 0);
                Worker w02 = new Worker(7, game, 1, 1);
                Worker w03 = new Worker(8, game, 2, 2);
                Worker w04 = new Worker(9, game, 3, 3);
                Worker w05 = new Worker(10, game, 4, 4);

                w01.start(); 
                w02.start();
                w03.start();
                w04.start();
                w05.start();

                w01.join(); 
                w02.join();
                w03.join();
                w04.join();
                w05.join();
                
                game.taskNo--;

            } catch (Exception e) {
                e.printStackTrace();
            }

            //System.out.println("\033[2J\033[H");
            game.printState();
        }
    }
}

class Worker extends Thread {

    private int no;
    public int firstRow;
    public int lastRow;

    GameOfLife game;

    public Worker(int no, GameOfLife game, int firstRow, int lastRow) {
        this.no = no;
        this.game = game;
        this.firstRow = firstRow;
        this.lastRow = lastRow;
    }

    public void run() {
        switch (game.taskNo) {
        case 1:       
            game.updateNeighbourCount(firstRow, lastRow);
            break;
        case 2:       
            game.updateCells(firstRow, lastRow);
            break;
        default:
            System.out.println("ERROR: There is no such task for a worker");
            System.exit(1);
        }
    }
}

class GameOfLife {
    private int rows;
    private int cols;

    private int grid[][];
    public int neighbourGrid[][];
    
    public int taskNo;

    public GameOfLife(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new int[rows][cols];
        this.neighbourGrid = new int[rows][cols];
        this.taskNo = 1;
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
                    grid[i][j] = 0;
                } else if (ncc == 2) {
                    if (grid[i][j] == 1)
                        grid[i][j] = 1;
                } else if (ncc == 3) {
                    grid[i][j] = 1;
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

    public void printState() {
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

    public void initStage(int stage) {
        switch (stage) {
        case 1:       // Block (period = 1, stays still)
            // TODO: min rows & cols validation (4x4)
            grid[1][1] = 1;
            grid[1][2] = 1;
            grid[2][1] = 1;
            grid[2][2] = 1;
            break;
        case 2:       // Blinker (period = 2, stays still)
            // TODO: min rows & cols validation (5x5)
            grid[1][2] = 1;
            grid[2][2] = 1;
            grid[3][2] = 1;
            break;
        case 3:       // Toad (period = 2, stays still)
            // TODO: min rows & cols validation (6x6)
            grid[1][3] = 1;
            grid[2][1] = 1;
            grid[2][4] = 1;
            grid[3][1] = 1;
            grid[3][4] = 1;
            grid[4][2] = 1;
            break;
        case 4:       // Pulsar (period = 3, stays still)
            // TODO: min rows & cols validation (17x17)
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

            // ==============
            grid[4][2] =   1;
            grid[4][7] =   1;
            grid[4][9] =   1;
            grid[4][14] =  1;
            // ==============
            grid[5][2] =   1;
            grid[5][7] =   1;
            grid[5][9] =   1;
            grid[5][14] =  1;
            // ==============
            grid[6][2] =   1;
            grid[6][7] =   1;
            grid[6][9] =   1;
            grid[6][14] =  1;
            // ==============
            grid[10][2] =  1;
            grid[10][7] =  1;
            grid[10][9] =  1;
            grid[10][14] = 1;
            // ==============
            grid[11][2] =  1;
            grid[11][7] =  1;
            grid[11][9] =  1;
            grid[11][14] = 1;
            // ==============
            grid[12][2] =  1;
            grid[12][7] =  1;
            grid[12][9] =  1;
            grid[12][14] = 1;
            // ==============

            break;
        case 5:       // Glider (period = 4, moves southeast)
            // TODO: min rows & cols validation (4x4)
            grid[0][2] = 1;
            grid[1][0] = 1;
            grid[1][2] = 1;
            grid[2][1] = 1;
            grid[2][2] = 1;
            break;
        case 6:       // Middle-weight spaceship (MWSS) (period = 4, moves east)
            // TODO: min rows & cols validation (10x10)
            grid[3][5] = 1;
            grid[3][6] = 1;

            grid[4][2] = 1;
            grid[4][3] = 1;
            grid[4][4] = 1;
            grid[4][6] = 1;
            grid[4][7] = 1;

            grid[5][2] = 1;
            grid[5][3] = 1;
            grid[5][4] = 1;
            grid[5][5] = 1;
            grid[5][6] = 1;

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

