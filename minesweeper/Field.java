package minesweeper;

import java.util.Scanner;

public class Field {
    final char MINE = 'X';
    final char SAFE = '.';
    final char MARK = '*';
    final char EMPTY_MARK = '/';

    private int row;
    private int colume;
    private int numOfMines;
    private boolean win = false;

    /*
     hidden_gameContent will be larger than player content square ( +2 ), if playerContent 9x9 then
     hidden_gameContent = 11x11, this will help with checks and other stuff
     we will use hidden_gameContent for Game Logic
    */
    char[][] hidden_gameContent;
    // and playersContent we will use to print for user
    char[][] playersContent;


    // only for initial.
    public Field(int row, int colume, int numOfMines) {
        this.row = row;
        this.colume = colume;
        this.numOfMines = numOfMines;
        // hidden_gameContent larger then player content
        this.hidden_gameContent = new char[row + 2][colume + 2];
        this.playersContent = new char[row][colume];

        // Fill playersContent with .
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < colume; j++) {
                playersContent[i][j] = SAFE;
            }
        }
// Fill hidden_gameContent with '/'
        for (int i = 0; i < hidden_gameContent.length; i++) {
            for (int j = 0; j < hidden_gameContent[i].length; j++) {
                if (hidden_gameContent[i][j] != MINE) {
                    hidden_gameContent[i][j] = EMPTY_MARK;
                }
            }
        }

    }

    void putMinesOnField(int playerRow, int playerColume) {

        int minesLeft = numOfMines;
        while (minesLeft != 0) {
            int tRow = (int) (Math.random() * this.row);
            int tColume = (int) (Math.random() * this.colume);
            if (hidden_gameContent[tRow + 1][tColume + 1] != MINE && tRow != playerRow && tColume != playerColume) {
                hidden_gameContent[tRow + 1][tColume + 1] = MINE;
                minesLeft--;
            }
        }
    }

    // Putting numbers about mines around and "cutting" edges on our hidden content with '9'
    void putNumbersOfMinesOnField(int playerRow, int playerColume) {
        char[][] gameField = hidden_gameContent;
        int[][] temp = new int[gameField.length][gameField[0].length];
        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField[0].length; j++) {
                if (gameField[i][j] == MINE) {

                    temp[i - 1][j - 1] += 1;

                    temp[i - 1][j] += 1;

                    temp[i - 1][j + 1] += 1;

                    temp[i][j - 1] += 1;

                    temp[i][j + 1] += 1;

                    temp[i + 1][j - 1] += 1;

                    temp[i + 1][j] += 1;

                    temp[i + 1][j + 1] += 1;

                }
            }
        }
        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField[0].length; j++) {
                if (temp[i][j] > 0) {
                    if (gameField[i][j] != MINE) gameField[i][j] = (char) (temp[i][j] + '0');
                }
            }
        }
        for (int i = 0; i < gameField.length; i++) {
            gameField[0][i] = '9';
        }
        for (int i = 0; i < gameField.length; i++) {
            gameField[gameField.length - 1][i] = '9';
        }
        for (int i = 1; i < gameField.length - 1; i++) {
            gameField[i][gameField.length - 1] = '9';
        }
        for (int i = 1; i < gameField.length - 1; i++) {
            gameField[i][0] = '9';
        }
        playersContent[playerRow][playerColume] = hidden_gameContent[playerRow + 1][playerColume + 1];

    }

    void gameLoop(Scanner sc) {

        int flags = 0;
        int checkedMines = 0;

        String flag;

        while (!win) {
            System.out.println("Set/unset mines marks or claim a cell as free: ");

            int check_Y = sc.nextInt() - 1;
            int check_X = sc.nextInt() - 1;

            flag = sc.nextLine().trim();

            switch (flag) {
                case "free":
                    if (hidden_gameContent[check_X + 1][check_Y + 1] == MINE) {
                        for (int i = 1; i < hidden_gameContent.length - 1; i++) {
                            for (int j = 1; j < hidden_gameContent[i].length; j++) {
                                if (hidden_gameContent[i][j] == MINE) playersContent[i - 1][j - 1] = MINE;
                            }
                        }
                        FieldPrinter.print(this);
                        System.out.println("You stepped on a mine and failed!");
                        return;
                    }
                    if (hidden_gameContent[check_X + 1][check_Y + 1] != '/') {
                        playersContent[check_X][check_Y] = hidden_gameContent[check_X + 1][check_Y + 1];
                        FieldPrinter.print(this);
                        break;
                    } else {
                        floodFill(hidden_gameContent, playersContent, new boolean[playersContent.length][playersContent[0].length], check_X, check_Y);
                        FieldPrinter.print(this);
                        break;
                    }
                case "mine":
                    if (playersContent[check_X][check_Y] != MARK) {
                        playersContent[check_X][check_Y] = MARK;
                        flags++;
                        if (hidden_gameContent[check_X + 1][check_Y + 1] == MINE) checkedMines++;
                    } else if (playersContent[check_X][check_Y] == MARK) {
                        playersContent[check_X][check_Y] = SAFE;

                        if (hidden_gameContent[check_X][check_Y] == MINE) checkedMines--;
                        flags--;
                    }

                    FieldPrinter.print(this);
                    break;
                default:
                    System.out.println("Wrong type!!!\nTry again");
            }
            checkWinningConditions(flags, checkedMines);

        }

        System.out.println("Congratulations! You found all mines!");

    }

    void checkWinningConditions(int flags, int checkedMines) {

        // check if all mines are flagged
        if (checkedMines == numOfMines && flags == numOfMines) win = true;

        // check if user ckeck all safe cells, living only the cells with mines unexplored / flagged
        int numChecksAndFlags = 0;
        for (int i = 0; i < playersContent.length; i++) {
            for (int j = 0; j < playersContent[0].length; j++) {
                if (playersContent[i][j] == MARK || playersContent[i][j] == SAFE) numChecksAndFlags++;
            }
        }
        if (numChecksAndFlags == numOfMines) win = true;
    }

    //Floodfill algorithm:
    static void floodFill(char[][] grid, char[][] gridForPlayer, boolean[][] visited, int r, int c) {
        //quit if off the grid:
        if (r < 0 || r >= gridForPlayer.length || c < 0 || c >= gridForPlayer[0].length) return;

        //quit if visited:
        if (visited[r][c]) return;
        visited[r][c] = true;

        //quit if hit number:
        // if(grid[r][c] >= '1' && grid[r][c] <= '8' ) return;
        if (grid[r + 1][c + 1] != '/') {
            gridForPlayer[r][c] = grid[r + 1][c + 1];
            return;
        } else gridForPlayer[r][c] = '/';


        //recursively fill in all directions
        floodFill(grid, gridForPlayer, visited, r + 1, c);
        floodFill(grid, gridForPlayer, visited, r + 1, c + 1);
        floodFill(grid, gridForPlayer, visited, r - 1, c);
        floodFill(grid, gridForPlayer, visited, r - 1, c - 1);
        floodFill(grid, gridForPlayer, visited, r, c + 1);
        floodFill(grid, gridForPlayer, visited, r - 1, c + 1);
        floodFill(grid, gridForPlayer, visited, r, c - 1);
        floodFill(grid, gridForPlayer, visited, r + 1, c - 1);
    }
}
