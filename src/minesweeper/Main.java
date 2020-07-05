package minesweeper;

import java.util.*;

public class Main {
    private static final Random RANDOM = new Random();
    private static final Scanner STDIN = new Scanner(System.in);
    private static final int FIELD_SIZE = 9;

    private final Cell[][] field = new Cell[FIELD_SIZE][FIELD_SIZE];
    private final Deque<Point> mineQueue = new ArrayDeque<>();

    private int numberOfMines;
    private boolean isFirstFreeCommand = true;
    private boolean steppedOnMine = false;

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        System.out.print("How many mines do you want on the field? ");
        numberOfMines = STDIN.nextInt();
        STDIN.nextLine(); // clear buffer
        createField();
        printField();

        do {
            doCommand();
        } while (!isGameFinished());

        if (steppedOnMine) {
            System.out.println("You stepped on a mine and failed!");
        } else {
            System.out.println("Congratulations! You found all mines!");
        }
    }

    private void createField() {
        for (int row = 0; row < FIELD_SIZE; row++) {
            for (int column = 0; column < FIELD_SIZE; column++) {
                field[row][column] = new Cell();
            }
        }
    }

    private void createMines(int currentRow, int currentColumn) {
        for (int i = 0; i < numberOfMines; i++) {
            boolean tryAgain = true;

            do {
                int row = RANDOM.nextInt(FIELD_SIZE);
                int column = RANDOM.nextInt(FIELD_SIZE);

                if (!field[row][column].isMine() && row != currentRow && column != currentColumn) {
                    field[row][column].setMine(true);
                    tryAgain = false;
                }
            } while (tryAgain);
        }
    }

    private void createClues() {
        for (int row = 0; row < field.length; row++) {
            for (int column = 0; column < field[row].length; column++) {
                if (field[row][column].isMine()) {
                    continue;
                }

                for (Neighbor neighbor : Neighbor.values()) {
                    if (isNeighborAMine(neighbor.getPoint(), row, column)) {
                        field[row][column].incrementNextToMines();
                        field[row][column].setRevealed(true);
                    }
                }
            }
        }
    }

    private boolean isNeighborAMine(Point point, int row, int column) {
        int x = column + point.getX();
        int y = row + point.getY();

        if (x >= 0 && x < FIELD_SIZE && y >= 0 && y < FIELD_SIZE) {
            return field[y][x].isMine();
        }

        return false;
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
                } else {
                    System.out.print(field[row][column - 2]);
                }
            }

            System.out.println();
        }

        System.out.println("-|---------|");
    }

    private void doCommand() {
        boolean thereIsMoreToDo;

        do {
            System.out.print("Set/unset mines marks or claim a cell as free: ");
            String input = STDIN.nextLine();

            if ("mine".equalsIgnoreCase(input)) {
                doDeleteMine();
                thereIsMoreToDo = false;
                continue;
            }

            String[] parts = input.split("\\s+");

            if (parts.length != 3) {
                System.out.println("Wrong number of arguments, try again");
                thereIsMoreToDo = true;
                continue;
            }

            int x = Integer.parseInt(parts[0]) - 1;
            int y = Integer.parseInt(parts[1]) - 1;

            if (x < 0 || x >= FIELD_SIZE || y < 0 || y >= FIELD_SIZE) {
                System.out.println("Coordinates must be between 1 and " + FIELD_SIZE);
                thereIsMoreToDo = true;
            } else if ("mine".equalsIgnoreCase(parts[2])) {
                doSetMine(x, y);
                thereIsMoreToDo = false;
            } else if ("free".equalsIgnoreCase(parts[2])) {
                doSetFree(x, y);
                thereIsMoreToDo = false;
            } else {
                thereIsMoreToDo = true;
            }
        } while (thereIsMoreToDo);

        printField();
    }

    private void doDeleteMine() {
        Point point = mineQueue.pollLast();

        if (point == null) {
            System.out.println("No more mine marks to remove");
        } else {
            field[point.getY()][point.getX()].setMarkedAsMine(false);
            field[point.getY()][point.getX()].setRevealed(false);
        }
    }

    private void doSetMine(int x, int y) {
        if (field[y][x].isNextToMines()) {
            System.out.println("There is a number here!");
        } else {
            field[y][x].setMarkedAsMine(true);
            field[y][x].setRevealed(true);
            mineQueue.offerLast(new Point(x, y));
        }
    }

    private void doSetFree(int x, int y) {
        if (isFirstFreeCommand) {
            isFirstFreeCommand = false;
            createMines(y, x);
            createClues();
        } else if (field[y][x].isMine()) {
            revealAll();
            steppedOnMine = true;
            return;
        }

        doSetFreeRecurse(x, y);
    }

    private void doSetFreeRecurse(int x, int y) {
        if (x < 0 || x >= FIELD_SIZE || y < 0 || y >= FIELD_SIZE) {
            return;
        }

        if (field[y][x].isMine() || field[y][x].isMarkedAsMine() || field[y][x].isNextToMines()
                || field[y][x].isMarkedAsFree()) {
            return;
        }

        field[y][x].setMarkedAsFree(true);
        field[y][x].setRevealed(true);

        for (Neighbor neighbor : Neighbor.values()) {
            // make local copies of x and y for recursion
            int column = x;
            int row = y;
            column += neighbor.getPoint().getX();
            row += neighbor.getPoint().getY();
            doSetFreeRecurse(column, row);
        }
    }

    private void revealAll() {
        for (int row = 0; row < FIELD_SIZE; row++) {
            for (int column = 0; column < FIELD_SIZE; column++) {
                field[row][column].setRevealed(true);
            }
        }
    }

    private boolean isGameFinished() {
        if (steppedOnMine) {
            return true;
        }

        int minesFound = 0;
        int freeOrClue = 0;

        for (int row = 0; row < FIELD_SIZE; row++) {
            for (int column = 0; column < FIELD_SIZE; column++) {
                if (field[row][column].isMarkedAsMine()) {
                    if (field[row][column].isMine()) {
                        minesFound++;
                    } else {
                        return false; // marked as a mine but not a mine
                    }
                } else if (field[row][column].isMarkedAsFree() || field[row][column].isNextToMines()) {
                    freeOrClue++;
                }
            }
        }

        return minesFound == numberOfMines || freeOrClue == FIELD_SIZE * FIELD_SIZE - numberOfMines;
    }
}
