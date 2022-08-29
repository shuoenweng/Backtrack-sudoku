/**
 * Sudoku.java
 * 
 * Implementation of a class that represents a Sudoku puzzle and solves
 * it using recursive backtracking.
 *
*/
import java.io.*;   
import java.util.*;

public class Sudoku {    
    // The current contents of the cells of the puzzle. 
    private int[][] grid;
    
    /*
     * Indicates whether the value in a given cell is fixed 
     */
    private boolean[][] valIsFixed;
    

    private boolean[][][] subgridHasVal;
    
    
    private boolean [][] rowVal;
    private boolean [][] colVal;
    
    /* 
     * Constructs a new Puzzle object, which initially
     * has all empty cells.
     */
    public Sudoku() {
        this.grid = new int[9][9];
        this.valIsFixed = new boolean[9][9];     
        this.subgridHasVal = new boolean[3][3][10];        
        this.rowVal = new boolean[9][10];
        this.colVal = new boolean[9][10];
    }
    
    /*
     * Place the specified value in the cell with the specified
     * coordinates, and update the state of the puzzle accordingly.
     */
    public void placeVal(int val, int row, int col) {
        this.grid[row][col] = val;
        this.subgridHasVal[row/3][col/3][val] = true;
        this.rowVal[row][val] = true;
        this.colVal[col][val] = true;
    }
        
    /*
     * remove the specified value from the cell with the specified
     * coordinates, and update the state of the puzzle accordingly.
     */
    public void removeVal(int val, int row, int col) {
        this.grid[row][col] = 0;
        this.subgridHasVal[row/3][col/3][val] = false;
        this.rowVal[row][val] = false;
        this.colVal[col][val] = false;
    }  
        
    /*
     * read in the initial configuration of the puzzle from the specified 
     * Scanner, and use that config to initialize the state of the puzzle.  
     * The configuration should consist of one line for each row, with the
     * values in the row specified as integers separated by spaces.
     * A value of 0 should be used to indicate an empty cell.
     * 
     */
    public void readConfig(Scanner input) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int val = input.nextInt();
                this.placeVal(val, r, c);
                if (val != 0) {
                    this.valIsFixed[r][c] = true;
                }
            }
            input.nextLine();
        }
    }
                
    /*
     * Displays the current state of the puzzle.
     */        
    public void printGrid() {
        for (int r = 0; r < 9; r++) {
            this.printRowSeparator();
            for (int c = 0; c < 9; c++) {
                System.out.print("|");
                if (this.grid[r][c] == 0) {
                    System.out.print("   ");
                } else {
                    System.out.print(" " + this.grid[r][c] + " ");
                }
            }
            System.out.println("|");
        }
        this.printRowSeparator();
    }
        
    // A private helper method used by display() 
    // to print a line separating two rows of the puzzle.
    private static void printRowSeparator() {
        for (int i = 0; i < 9; i++) {
            System.out.print("----");
        }
        System.out.println("-");
    }
    
    


    //Checks if input is valid.
    private boolean isVal(int r, int c, int n){
        if((this.rowVal[r][n] == false) && (this.colVal[c][n] == false) && (this.subgridHasVal[r/3][c/3][n] == false)){
            return true;
        } 
        else {
            return false;
        }
    }
    
         
    /*
     * This is the key recursive-backtracking method.  Returns true if
     * a solution has already been found, and false otherwise.
     * 
     * Each invocation of the method is responsible for finding the
     * value of a single cell of the puzzle. The parameter n
     * is the number of the cell that a given invocation of the method
     * is responsible for. We recommend that you consider the cells
     * one row at a time, from top to bottom and left to right,
     * which means that they would be numbered as follows:
     *
     *     0  1  2  3  4  5  6  7  8
     *     9 10 11 12 13 14 15 16 17
     *    18 ...
     */
    private boolean solveRB(int n) {
        int c = n /9;
        int r = n %9;             
     if(n >= 81) {
        return true;
    }
    else if(this.valIsFixed[r][c]==true ){
        return this.solveRB(n+1);
    }
    int i = 1;
            while(i <=9){
                if(this.isVal(r, c, i) == true){
                    this.placeVal(i, r, c);
                    if(this.solveRB(n+1)){
                        return true;
                    }
                    this.removeVal(i, r, c);
                }
                i++;
            }
            
            return false;
            
    
        }
    
            

    
    /*
     * public "wrapper" method for solveRB().
     * Makes the initial call to solveRB, and returns whatever it returns.
     */
    public boolean solve() { 
        boolean foundSol = this.solveRB(0);
        return foundSol;
    }
    
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Sudoku puzzle = new Sudoku();
        
        System.out.print("Enter the name of the puzzle file: ");
        String filename = scan.nextLine();
        
        try {
            Scanner input = new Scanner(new File(filename));
            puzzle.readConfig(input);
        } catch (IOException e) {
            System.out.println("error accessing file " + filename);
            System.out.println(e);
            System.exit(1);
        }
        
        System.out.println();
        System.out.println("Here is the initial puzzle: ");
        puzzle.printGrid();
        System.out.println();
        
        if (puzzle.solve()) {
            System.out.println("Here is the solution: ");
        } else {
            System.out.println("No solution could be found.");
            System.out.println("Here is the current state of the puzzle:");
        }
        puzzle.printGrid();  
    }    
}
