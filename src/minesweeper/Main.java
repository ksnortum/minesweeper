package minesweeper;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final Random RANDOM = new Random();
    private static final Scanner STDIN = new Scanner(System.in);
    private static final int FIELD_SIZE = 9;

    private final char[][] field = new char[FIELD_SIZE][FIELD_SIZE];
    private final char[][] mines = new char[FIELD_SIZE][FIELD_SIZE];
    private final boolean debug = false;

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        System.out.print("How many mines do you want on the field? ");
        int numberOfMines = STDIN.nextInt();
        createMines(numberOfMines);
        createField();
        createClues();
        printField();
        boolean gameIsNotFinished = true;

        do {
            setDeleteMark();
            gameIsNotFinished = isGameStillGoing(numberOfMines);
        } while (gameIsNotFinished);

        System.out.println("Congratulations! You found all mines!");
    }

    private void createMines(int numberOfMines) {
        for (int i = 0; i < numberOfMines; i++) {
            boolean tryAgain = true;

            do {
                int row = RANDOM.nextInt(FIELD_SIZE);
                int column = RANDOM.nextInt(FIELD_SIZE);

                if (mines[row][column] != 'X') {
                    mines[row][column] = 'X';
                    tryAgain = false;
                }
            } while (tryAgain);
        }
    }

    private void createField() {
        for (char[] chars : field) {
            Arrays.fill(chars, '.');
        }
    }

    private void createClues() {
        for (int row = 0; row < field.length; row++) {
            for (int column = 0; column < field[row].length; column++) {
                if (mines[row][column] == 'X') {
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
        int x = column + point.getX();
        int y = row + point.getY();

        if (x >= 0 && x < FIELD_SIZE && y >= 0 && y < FIELD_SIZE) {
            cell = mines[y][x];
        }

        return cell;
    }

    private void printField() {
        System.out.println(" |123456789|");
        System.out.println("-|---------|");

        for (int row = 0; row < FIELD_SIZE; row++) {
            for (int column = 0; column < FIELD_SIZE + 3; column++) {
                if (column == 0) {
                    System.out.print(row + 1);
                } else if (column == 1 || column == FIELD_SIZE + 2) {
                    System.out.print("|");
                } else if (debug && mines[row][column - 2] == 'X') {
                    System.out.print('X');
                } else {
                    System.out.print(field[row][column - 2]);
                }
            }

            System.out.println();
        }

        System.out.println("-|---------|");
    }

    private void setDeleteMark() {
        boolean thereIsMoreToDo = false;

        do {
            System.out.print("Set/delete mines marks (x and y coordinates): ");
            int x = STDIN.nextInt() - 1;
            int y = STDIN.nextInt() - 1;

            if (x < 0 || x >= FIELD_SIZE || y < 0 || y >= FIELD_SIZE) {
                System.out.println("Coordinates must be between 1 and " + FIELD_SIZE);
                thereIsMoreToDo = true;
            } else if (Character.isDigit(field[y][x])) {
                System.out.println("There is a number here!");
                thereIsMoreToDo = true;
            } else if (field[y][x] == '*') {
                field[y][x] = '.';
                thereIsMoreToDo = false;
            } else {
                field[y][x] = '*';
                thereIsMoreToDo = false;
            }

        } while (thereIsMoreToDo);

        printField();
    }

    // TODO, something wrong here
    private boolean isGameStillGoing(int numberOfMines) {
        int minesFound = 0;

        for (int row = 0; row < FIELD_SIZE; row++) {
            for (int column = 0; column < FIELD_SIZE; column++) {
                if (field[row][column] == '*') {
                    if (mines[row][column] != 'X') {
                        return true;
                    } else {
                        minesFound++;
                    }
                }
            }
        }

        return minesFound != numberOfMines;
    }
}
