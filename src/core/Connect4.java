package core;

/**
 * This program is a simulation of Connect4 game. This class updates the grid
 * and check if anyone wins.
 * 
 * @author Xiaolou Huang (Lor)
 * @version 9/29/2018
 *
 */
public class Connect4 {

    public static enum gameState {
        INVALID, FULL, DRAW, CONTINUE, XWin, OWin, XTurn, OTurn;
    } // the states of the game.

    public char[][] grid; // the board
    public int rows = 6; // rows of the board
    public int cols = 7; // columns of the board

    public Connect4() {
        grid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = ' ';
            }
        }
    }

    /**
     * This method check if the col is valid and use checkWinState() function to
     * check if anyone wins.
     * 
     * @param column
     *            the column to add the coin.
     * @param player
     *            the current player
     * @return return the state of the game.
     * 
     */
    public gameState updateGrid(int column, char player) {

        boolean winState;
        int col = column;

        // check if the column is within 0-6
        if (col < 0 || col > 6) {
            return gameState.INVALID;
        }

        // check if the column is full
        if (grid[0][col] == 'X' || grid[0][col] == 'O') {
            return gameState.FULL;
        }

        // check if it is a draw
        boolean isDraw = true;
        for (int i = 0; i < rows; i++) {
            if (grid[i][col] == ' ') {
                isDraw = false;
            }
        }
        if (isDraw == true) {
            return gameState.DRAW;
        }

        // check if anyone wins
        winState = checkWinState(col, player);
        if (winState == true) {
            if (player == 'X') {
                return gameState.XWin;
            } else {
                return gameState.OWin;
            }
        }
        
        // if no one wins, return whose turn
        if (player == 'X') {
            return gameState.OTurn;
        } else if (player == 'O') {
            return gameState.XTurn;
        }

        // still playing, no one wins.
        return gameState.CONTINUE;
    }

    /**
     * This method is a helper function to updateGrid(). It adds the coins into the
     * grid. Then, checks horizontally, vertically and diagonally to see if there is
     * a win.
     * 
     * @param col
     *            the column to add coin.
     * @param player
     *            the player (X or O).
     * @return true if there is a win, false otherwise.
     */
    private boolean checkWinState(int col, char player) {

        // first add the coin into grid.
        int rowCount = rows - 1;
        while (grid[rowCount][col] != ' ') {
            rowCount--;
        }
        grid[rowCount][col] = player;

        /* check grid horizontally. */
        int countX = 0;
        int countO = 0;
        // check every column in each row if there is a win.
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 'X') {
                    // check if previous coin is the same as this one.
                    if (j == 0) {
                        countX++;
                    } else if (j > 0 && (grid[i][j - 1] != 'X')) {
                        countX = 1;
                        countO = 0;
                    } else if (j > 0 && (grid[i][j - 1] == 'X')) {
                        countX++;
                    } else {
                        countX = 0;
                        countO = 0;
                    }
                }
                // check if previous coin is the same as this one.
                else if (grid[i][j] == 'O') {
                    if (j == 0) {
                        countO++;
                    } else if (j > 0 && (grid[i][j - 1] != 'O')) {
                        countO = 1;
                        countX = 0;
                    } else if (j > 0 && (grid[i][j - 1] == 'O')) {
                        countX++;
                    } else {
                        countX = 0;
                        countO = 0;
                    }
                } else {
                    countX = 0;
                    countO = 0;
                }
                // if 4 same coins appear.
                if (countX == 4 || countO == 4) {
                    System.out.println("Horizonally");
                    return true;
                }
            }
            countX = 0;
            countO = 0;
        }

        /* check grid vertically. */
        // reset countX & countO.
        countX = 0;
        countO = 0;
        // check every row in each column if there is a win.
        for (int i = 0; i < cols; i++) {
            for (int j = rows - 1; j >= 0; j--) {
                if (grid[j][i] == 'X') {
                    // check if previous coin is the same as this one.
                    if (j == rows - 1) {
                        countX++;
                    } else if (j < rows - 1 && (grid[j + 1][i] != 'X')) {
                        countX = 1;
                    } else if ((j < rows - 1) && (grid[j + 1][i] == 'X')) {
                        countX++;
                    } else {
                        countX = 0;
                        countO = 0;
                    }
                }
                // check if previous coin is the same as this one.
                else if (grid[j][i] == 'O') {
                    if (j == rows - 1) {
                        countO++;
                    } else if ((j < rows - 1) && (grid[j + 1][i] != 'O')) {
                        countO = 1;
                    } else if ((j < rows - 1) && (grid[j + 1][i] == 'O')) {
                        countO++;
                    } else {
                        countX = 0;
                        countO = 0;
                    }
                } else {
                    countX = 0;
                    countO = 0;
                }
                // if 4 same coins appear.
                if (countX == 4 || countO == 4) {
                    System.out.println("Vertically");
                    return true;
                }
            }
            countX = 0;
            countO = 0;
        }

        /* check grid diagonally. */
        // check left to right, top to bottom diagonal.
        // reset countX & countO.
        countX = 0;
        countO = 0;
        for (int i = 0; i < rows - 3; i++) {
            for (int j = 0; j < cols - 3; j++) {
                for (int k = 0; k < 4; k++) {
                    if (grid[i + k][j + k] == 'X') {
                        // check if the previous is the same as this one.
                        if (k == 0) {
                            countX++;
                        } else if (k > 0 && grid[i + k - 1][j + k - 1] == 'X') {
                            countX++;
                        } else {
                            countX = 0;
                            countO = 0;
                        }
                    }
                    // check if the previous is the same as this one.
                    else if (grid[i + k][j + k] == 'O') {
                        if (k == 0) {
                            countO++;
                        } else if (k > 0 && grid[i + k - 1][j + k - 1] == 'O') {
                            countO++;
                        } else {
                            countX = 0;
                            countO = 0;
                        }
                    } else {
                        countX = 0;
                        countO = 0;
                    }
                    // if 4 same coins appear.
                    if (countX == 4 || countO == 4) {
                        System.out.println("Diagonal, L-R, Top-Bom");
                        return true;
                    }
                }
            }
        }

        // check left to right, bottom to top diagonal.
        // reset countX & countO.
        countX = 0;
        countO = 0;
        for (int i = rows - 1; i > 3; i--) {
            for (int j = 0; j < cols - 3; j++) {
                for (int k = 0; k < 4; k++) {
                    if (grid[i - k][j + k] == 'X') {
                        // check if the previous is the same as this one.
                        if (k == 0) {
                            countX++;
                        } else if (k > 0 && grid[i - k + 1][j + k - 1] == 'X') {
                            countX++;
                        } else {
                            countX = 0;
                            countO = 0;
                        }
                    } else if (grid[i - k][j + k] == 'O') {
                        // check if the previous is the same as this one.
                        if (k == 0) {
                            countO++;
                        } else if (k > 0 && grid[i - k + 1][j + k - 1] == 'O') {
                            countO++;
                        } else {
                            countX = 0;
                            countO = 0;
                        }
                    } else {
                        countX = 0;
                        countO = 0;
                    }
                    // if 4 same coins appear.
                    if (countX == 4 || countO == 4) {
                        System.out.println("Diagonal, L-R, Bom-Top");
                        return true;
                    }
                }
            }
        }

        // check right to left, top to bottom diagonal.
        // reset countX & countO.
        countX = 0;
        countO = 0;
        for (int i = 0; i < rows - 3; i++) {
            for (int j = cols - 1; j > 3; j--) {
                for (int k = 0; k < 4; k++) {
                    if (grid[i + k][j - k] == 'X') {
                        // check if the previous is the same as this one.
                        if (k == 0) {
                            countX++;
                        } else if (k > 0 && grid[i + k - 1][j - k + 1] == 'X') {
                            countX++;
                        } else {
                            countX = 0;
                            countO = 0;
                        }
                    }
                    // check if the previous is the same as this one.
                    else if (grid[i + k][j - k] == 'O') {
                        if (k == 0) {
                            countO++;
                        } else if (k > 0 && grid[i + k - 1][j - k + 1] == 'O') {
                            countO++;
                        } else {
                            countX = 0;
                            countO = 0;
                        }
                    } else {
                        countX = 0;
                        countO = 0;
                    }
                    // if 4 same coins appear
                    if (countX == 4 || countO == 4) {
                        System.out.println("Diagonal, R-L, Top-Bom");
                        return true;
                    }
                }
            }
        }

        // check right to left, bottom to top diagonal.
        // reset countX & countO.
        countX = 0;
        countO = 0;
        for (int i = rows - 1; i > 3; i--) {
            for (int j = cols - 1; j > 3; j--) {
                for (int k = 0; k < 4; k++) {
                    if (grid[i - k][j - k] == 'X') {
                        // check if the previous is the same as this one.
                        if (k == 0) {
                            countX++;
                        } else if (k > 0 && grid[i - k + 1][j - k + 1] == 'X') {
                            countX++;
                        } else {
                            countX = 0;
                            countO = 0;
                        }
                    } else if (grid[i - k][j - k] == 'O') {
                        // check if the previous is the same as this one.
                        if (k == 0) {
                            countO++;
                        } else if (k > 0 && grid[i - k + 1][j - k + 1] == 'O') {
                            countO++;
                        } else {
                            countX = 0;
                            countO = 0;
                        }
                    } else {
                        countX = 0;
                        countO = 0;
                    }
                    // if 4 same coins appear
                    if (countX == 4 || countO == 4) {
                        System.out.println("Diagonal, R-L, Bom-Top");
                        return true;
                    }
                }
            }
        }

        // no one wins for now.
        return false;
    }

    /**
     * return rows.
     * @return rows
     */
    public int getRows() {
        return rows;
    }
    
    /**
     * return columns.
     * @return cols
     */
    public int getColumns() {
        return cols;
    }
    
    public int getFirstEmptyRow(int col) {
        int coln = col;
        for (int i = rows - 1; i >= 0; i--) {
            if (grid[i][coln] == ' ') {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Print the board (grid) to the console.
     */
    public void printGrid() {
        for (int i = 0; i < rows; i++) {
            System.out.print('|');
            for (int j = 0; j < cols; j++) {
                System.out.print(grid[i][j]);
                System.out.print('|');
            }
            System.out.println();
        }
    }

}
