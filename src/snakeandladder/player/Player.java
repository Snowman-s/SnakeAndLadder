package snakeandladder.player;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int gridNumber;
    private final int maxGridNumber;

    private Player(int maxGridNumber) {
        this.maxGridNumber = maxGridNumber;
    }

    public static List<Player> createPlayerList(int playerNum, int maxGridNumber) {
        List<Player> players = new ArrayList<>();

        for (int i = 0; i < playerNum; i++) {
            players.add(new Player(maxGridNumber));
        }

        return List.copyOf(players);
    }

    public int getGridNumber() {
        return gridNumber;
    }

    public void addGridNumber(int addGridNumber) {
        this.gridNumber = Integer.min(addGridNumber + gridNumber, maxGridNumber);
    }

    @Override
    public String toString() {
        return "Player{" +
                "grid:(" + gridNumber +
                "/" + maxGridNumber +
                ")}";
    }
}
