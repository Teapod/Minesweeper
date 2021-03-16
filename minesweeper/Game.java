package minesweeper;

import java.util.Scanner;

public class Game {
    static Field initiation(Scanner scanner){

        System.out.print("How many mines do you want on the field? 10 or less ");

        int numOfMines = 0;
        do {
            try {
                int temp = Integer.valueOf(scanner.nextLine());
                numOfMines = temp;
                if (numOfMines > 1) break;
                System.out.println("Sweetie, please, input number from 1 to 10");
            } catch (RuntimeException e) {
                System.out.println("Sweetie, please, input number from 1 to 10");
            }
        } while (numOfMines < 1 ) ;

        if (numOfMines > 10) numOfMines = 10;
        Field game = new Field(9, 9, numOfMines);
        FieldPrinter.print(game);
        System.out.println("First move is always safe!");
        System.out.println(
                "Write where u want to step in format \"d d\" like \"2 3\" where 2 is for column and 3 is for row");

        int players_first_check_colume = scanner.nextInt() - 1;
        int players_first_check_row = scanner.nextInt() - 1;

        game.putMinesOnField(players_first_check_row, players_first_check_colume);

        game.putNumbersOfMinesOnField(players_first_check_row, players_first_check_colume);

        Field.floodFill(
                game.hidden_gameContent, game.playersContent,
                new boolean[game.playersContent.length][game.playersContent[0].length],
                players_first_check_row, players_first_check_colume);

        FieldPrinter.print(game);

        return game;
    }

}
