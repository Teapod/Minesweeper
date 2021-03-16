package minesweeper;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("How many mines do you want on the field? 10 or less ");

        int numOfMines = 0;
        do {
            try {
                int temp = Integer.valueOf(scanner.nextLine());
                numOfMines = temp;
                if (numOfMines > 1) break;
            } catch (RuntimeException e) {
                System.out.println("WRONG!!! Please input number from 1 to 10");
            }
        } while (numOfMines < 1 ) ;


    }
}
