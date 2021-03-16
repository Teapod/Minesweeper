package minesweeper;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        Field game = Game.initiation(scanner);

        game.gameLoop(scanner);

        scanner.close();

    }
}

