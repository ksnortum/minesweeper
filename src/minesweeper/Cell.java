package minesweeper;

public class Cell {
    private boolean revealed = false;
    private boolean mine = false;
    private boolean markedAsFree = false;
    private boolean markedAsMine = false;
    private int nextToMines = 0;

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public boolean isMarkedAsFree() {
        return markedAsFree;
    }

    public void setMarkedAsFree(boolean markedAsFree) {
        this.markedAsFree = markedAsFree;
    }

    public boolean isMarkedAsMine() {
        return markedAsMine;
    }

    public void setMarkedAsMine(boolean markedAsMine) {
        this.markedAsMine = markedAsMine;
    }

    public int getNextToMines() {
        return nextToMines;
    }

    public void incrementNextToMines() {
        nextToMines++;
    }

    public boolean isNextToMines() {
        return nextToMines > 0;
    }

    @Override
    public String toString() {
        String display = ".";

        if (isRevealed()) {
            if (isMarkedAsMine()) {
                display = "*";
            } else if (isMarkedAsFree()) {
                display = "/";
            } else if (getNextToMines() > 0) {
                display = String.valueOf(getNextToMines());
            } else if (isMine()) {
                display = "X";
            }
        }

        return display;
    }
}
