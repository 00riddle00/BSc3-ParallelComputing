
//package lab3.parallel_test;

import java.util.Random;

public class Main {

   //----------------------------------------------------------
   // Global parameters, changeable before runtime
   static int gameGridSize = 100; 
   static int gameStartingStage= 0;
   //
   // Global parameters, input by user (at runtime)
   static int nThreads = 0; // Number of working threads
   static int workload = 0; // Workload means no. of iterations
   //----------------------------------------------------------
   
   // Make tests for given workload and number threads
   // Returns working time
    static double makePerformanceTest() throws Exception {

        GameOfLife game = new GameOfLife(gameGridSize, gameGridSize, workload);
        Barrier barrier = new Barrier(game, nThreads);

        game.initStage(gameStartingStage);
        //game.printState(false);
        
        long start = System.currentTimeMillis();

        // Create and start threads
        Worker[] workerThreads = new Worker[nThreads];

        int localWorkload = gameGridSize / nThreads;

        for (int i = 0; i < nThreads; i++) {
            (workerThreads[i] = 
                new Worker(i+1, barrier, game, localWorkload * i, localWorkload * (i+1) - 1)
            ).start();
        }
 
        // Wait until all threads finish
        for (int i = 0; i < nThreads; i++) {
            while (!workerThreads[i].finished) {
                workerThreads[i].join();
            }
        }

        long finish = System.currentTimeMillis();
        double timeElapsed = (finish - start)/1000.;

        game.printState(false);

        //System.out.println("Time: " + timeElapsed + "s");
        return timeElapsed;
   }

    public static void main(String[] args) {
        try {
            if (args.length < 2) {
                System.out.println("ERROR: Too few arguments");
                System.exit(1);
            } else if (! ((nThreads = Integer.parseInt(args[0])) >=1 && nThreads <= 24 && 
                          (workload = Integer.parseInt(args[1])) >=1 && workload <= 100000000)) {
                System.out.println("ERROR: problem with arguments");
                System.exit(1);
            } else {
                System.err.println("#Test for: nThreads="+nThreads+" workload="+workload);
                double dtime = makePerformanceTest();
                System.err.println("#Completed. Running time: " + dtime + "s");
                System.out.println( nThreads + " " + workload  + " " +dtime );
                System.exit(0);
            }
        } catch (Exception exc) {
            System.out.println(exc);
            exc.printStackTrace();
            System.exit(1);
        }
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
                game.iterPassed++;
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

    volatile boolean finished = false;
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
                
                game.updateNeighbourCount(firstRow, lastRow, i);
                //barrier.waitBarrier(1);

                game.updateCells(firstRow, lastRow, i);
                barrier.waitBarrier(2);
            }
            this.finished = true;

        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class GameOfLife {
    private int rows;
    private int cols;

    private int neighbourGrid[][];
    private int grid[][];
    private int tempGrid[][];
    private int newGrid[][];
    private int currGrid[][];
    public int iterCount;
    public int iterPassed;

    public GameOfLife(int rows, int cols, int iterCount) {
        this.rows = rows;
        this.cols = cols;
        this.iterCount = iterCount;
        this.grid = new int[rows][cols];
        this.tempGrid = new int[rows][cols];
        this.neighbourGrid = new int[rows][cols];
    }

    public void updateNeighbourCount(int firstRow, int lastRow, int iteration) {
        // neighbour cell count
        int ncc;

        if (iteration % 2 == 1) {
            currGrid = grid;
        } else {
            currGrid = tempGrid;
        }

        for (int i = firstRow; i <= lastRow; i++) {
            for (int j = 0; j < cols; j++) {

                // i = row, j = column
                ncc = 0;

                if ((j-1) >= 0)
                    ncc += currGrid[i][j-1];

                if ((j+1) < cols)
                    ncc += currGrid[i][j+1];

                if ((i-1) >= 0) {
                    ncc += currGrid[i-1][j];

                    if ((j-1) >= 0) 
                        ncc += currGrid[i-1][j-1];

                    if ((j+1) < cols)
                        ncc += currGrid[i-1][j+1];
                }

                if ((i+1) < rows) {
                    ncc += currGrid[i+1][j];
                    if ((j-1) >= 0)
                        ncc += currGrid[i+1][j-1];

                    if ((j+1) < cols)
                        ncc += currGrid[i+1][j+1];
                }

                neighbourGrid[i][j] = ncc;
            }
        }
    }

    public void updateCells(int firstRow, int lastRow, int iteration) {
        // neighbour cell count
        int ncc;

        if (iteration % 2 == 1) {
            currGrid = grid;
            newGrid = tempGrid;
        } else {
            currGrid = tempGrid;
            newGrid = grid;
        }

        for (int i = firstRow; i <= lastRow; i++) {
            for (int j = 0; j < cols; j++) {
                ncc = neighbourGrid[i][j];

                if (ncc < 2 || ncc > 3) {
                    newGrid[i][j] = 0;
                } else if (ncc == 2) {
                    if (currGrid[i][j] == 1) {
                        newGrid[i][j] = 1;
                    } else {
                        newGrid[i][j] = 0;
                    }
                } else if (ncc == 3) {
                    newGrid[i][j] = 1;
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

        if (iterPassed % 2 == 1) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < rows; j++) {
                    if (tempGrid[i][j] == 1) {
                        System.out.print("|O");
                    } else {
                        System.out.print("|_");
                    }
                }
                System.out.println("|");
            }
        } else {
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

