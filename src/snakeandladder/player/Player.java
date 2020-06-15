package snakeandladder.player;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int gridNumber;
    private final int lastGridNumber;

    private Player(int lastGridNumber) {
        this.lastGridNumber = lastGridNumber;
    }

    public static List<Player> createPlayerList(int playerNum, int lastGridNumber) {
        List<Player> players = new ArrayList<>();

        for (int i = 0; i < playerNum; i++) {
            players.add(new Player(lastGridNumber));
        }

        return players;
    }

    public int getGridNumber() {
        return gridNumber;
    }

    public void addGridNumber(int addGridNumber) {
        this.gridNumber = Integer.min(addGridNumber + gridNumber, lastGridNumber);
    }

    public void setGridNumber(int gridNumber) {
        this.gridNumber = Integer.min(gridNumber, lastGridNumber);
    }

    @Override
    public String toString() {
        return "Player{" +
                "grid:(" + gridNumber +
                "/" + lastGridNumber +
                ")}";
    }
}
