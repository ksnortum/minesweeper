package minesweeper;

import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final Random RANDOM = new Random();
    private static final Scanner STDIN = new Scanner(System.in);
    private static final int FIELD_SIZE = 9;

    private final char[][] field = new char[FIELD_SIZE][FIELD_SIZE];

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        System.out.print("How many mines do you want on the field? ");
        int numberOfMines = STDIN.nextInt();
        char[][] mines = createMines(numberOfMines);
        createField(mines);
        createClues();
        printField();
    }

    private char[][] createMines(int numberOfMines) {
        char[][] mines = new char[9][9];

        for (int i = 0; i < numberOfMines; i++) {
            boolean tryAgain = true;

            do {
                int row = RANDOM.nextInt(9);
                int column = RANDOM.nextInt(9);

                if (mines[row][column] != 'X') {
                    mines[row][column] = 'X';
                    tryAgain = false;
                }
            } while (tryAgain);
        }

        return mines;
    }

    private void createField(char[][] mines) {
        for (int row = 0; row < field.length; row++) {
            for (int column = 0; column < field[row].length; column++) {
                boolean isAMine = mines[row][column] == 'X';
                field[row][column] = isAMine ? 'X' : '.';
            }
        }
    }

    private void createClues() {
        for (int row = 0; row < field.length; row++) {
            for (int column = 0; column < field[row].length; column++) {
                if (field[row][column] == 'X') {
                    continue;
                }

                for (Neighbor neighbor : Neighbor.values()) {
                    if (getCell(neighbor.getPoint(), row, column) == 'X') {
                        if (field[row][column] == '.') {
                            field[row][column] = '1';
                        } else {
                            field[row][column]++;
                        }
                    }
                }
            }
        }

    }

    private char getCell(Point point, int row, int column) {
        char cell = '\0'; // invalid
        int x = row + point.getX();
        int y = column + point.getY();

        if (x >= 0 && x < FIELD_SIZE && y >= 0 && y < FIELD_SIZE) {
            cell = field[x][y];
        }

        return cell;
    }

    private void printField() {
        for (char[] chars : field) {
            for (char aChar : chars) {
                System.out.print(aChar);
            }

            System.out.println();
        }
    }
}
