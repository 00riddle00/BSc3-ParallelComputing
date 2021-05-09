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
 * gautuosius rezultatus. Atsiskaitant, pateikti (motyvuotą) sprendimo aprašą, įtraukiant
 * minetųjų priklausomybių grafikus
 *
 */

package lab3.sequential;

import java.util.Random;

public class Main {

	public static void main(String[] args) {
        int rows = 5;
        int cols = 5;
        int iterCount = 10;
        int stage = 0;
        
        GameOfLife game = new GameOfLife(rows, cols);
        game.initStage(stage);
        game.run(iterCount);
    }
}

class GameOfLife {
    private int rows;
    private int cols;

    private int grid[][];
    private int newGrid[][];

    public GameOfLife(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new int[rows][cols];
    }

    private void updateCells() {
        newGrid = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                updateCell(i, j);
            }
        }

        grid = newGrid;
    }

    private void updateCell(int i, int j) {
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

        if (ncc < 2 || ncc > 3) {
            newGrid[i][j] = 0;
        } else if (ncc == 2) {
            if (grid[i][j] == 1)
                newGrid[i][j] = 1;
        } else if (ncc == 3) {
            newGrid[i][j] = 1;
        }
    }

    private void printState() {
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

    public void run(int iterCount) {
        System.out.println("\033[2J\033[H");
        printState();

        for (int i = 0; i < iterCount; i++) {
            try {
                Thread.sleep(1000);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            updateCells();

            System.out.println("\033[2J\033[H");

            printState();
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

