package snakeandladder;

import java.util.*;
import java.util.stream.Stream;

/**
 * へびとはしごのフィールド
 */
public class Field {
    private final Set<GridPair> ladderAndSnake = new HashSet<>();
    private final int gridNum;
    private final int snakeNum;
    private final int ladderNum;

    private Field(Field.Builder builder) {
        Random random = new Random(builder.seed);

        this.gridNum = builder.gridNum;

        if (builder.randomSnakeAndLadder) {
            int snakeAndLadderNum = this.gridNum / 10;
            this.snakeNum = random.nextInt(snakeAndLadderNum);
            this.ladderNum = snakeAndLadderNum - this.snakeNum;
        } else {
            this.snakeNum = builder.snakeNum;
            this.ladderNum = builder.ladderNum;
        }

        List<Integer> remainGrid = new ArrayList<>();

        for (int i = 2; i < gridNum; i++) {
            remainGrid.add(i);
        }

        Collections.shuffle(remainGrid, random);

        for (int i = 0; i < this.snakeNum; i++) {
            int from = remainGrid.remove(0);
            int to = random.nextInt(from - 1) + 1;
            ladderAndSnake.add(new GridPair(from, to));
        }

        for (int i = 0; i < this.ladderNum; i++) {
            int from = remainGrid.remove(0);
            int to = random.nextInt(gridNum - from) + from + 1;
            ladderAndSnake.add(new GridPair(from, to));
        }
    }

    @Override
    public String toString() {
        return "Field{\n" +
                "ladderAndSnake=" + ladderAndSnake + "\n" +
                ", gridNum=" + gridNum + "\n" +
                ", snakeNum=" + snakeNum + "\n" +
                ", ladderNum=" + ladderNum + "\n" +
                '}';
    }

    public static class Builder {
        //フィールド生成シード
        private final long seed;

        //ますめの数
        private int gridNum = 100;
        //へびの数
        private int snakeNum = 5;
        //はしごの数
        private int ladderNum = 5;
        //蛇と梯子がランダムか
        private boolean randomSnakeAndLadder = false;

        public Builder(long seed) {
            this.seed = seed;
        }

        public Builder gridNum(int gridNum) {
            if (gridNum <= 1 || 100 < gridNum)
                throw new IllegalArgumentException("gridSize must be 1 < gridSize <= 100:" + gridNum);
            this.gridNum = gridNum;
            return this;
        }

        public Builder snakeNum(int snakeNum) {
            if (snakeNum < 0)
                throw new IllegalArgumentException("snakeNum must be 0 < snakeNum:" + snakeNum);
            this.snakeNum = snakeNum;
            return this;
        }

        public Builder ladderNum(int ladderNum) {
            if (ladderNum < 0)
                throw new IllegalArgumentException("ladderNum must be 0 < ladderNum:" + ladderNum);
            this.ladderNum = ladderNum;
            return this;
        }

        public Builder randomiseSnakeAndLadder() {
            randomSnakeAndLadder = true;
            return this;
        }

        public Field build() {
            if (!randomSnakeAndLadder) {
                //へび/はしごの数の和が [升目の数-2] 以下か？
                if (snakeNum + ladderNum > gridNum - 2) throw new IllegalStateException();
            }
            return new Field(this);
        }
    }

    private static class GridPair {
        private final int from, to;

        public GridPair(int from, int to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GridPair gridPair = (GridPair) o;
            return from == gridPair.from &&
                    to == gridPair.to;
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }

        @Override
        public String toString() {
            return "GridPair{" + from + "->" +  to + '}';
        }
    }
}