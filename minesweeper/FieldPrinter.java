package minesweeper;

public class FieldPrinter {

   static void print(Field field) {
        System.out.print(" |123456789|\n-|---------|\n");
        for (int i = 0; i < field.playersContent.length; i++) {
            System.out.print(i + 1 + "|");
            for (int j = 0; j < field.playersContent[0].length; j++) {
                System.out.print(field.playersContent[i][j]);
            }
            System.out.print("|\n");
        }
        System.out.println("-|---------|");
    }

   static void print_hidden(Field field) {
        System.out.print(" |123456789|\n-|---------|\n");
        for (int i = 0; i < field.hidden_gameContent.length; i++) {
            System.out.print(i + 1 + "|");
            for (int j = 0; j < field.hidden_gameContent[0].length; j++) {
                System.out.print(field.hidden_gameContent[i][j]);
            }
            System.out.print("|\n");
        }
        System.out.println("-|---------|");
    }
}
