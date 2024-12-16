import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Six {
    public static void main(String[] args) {
        List<String> input = getInput("resources/input6");

        int currRow = 0;
        int currCol = 0;
        List<List<Character>> map = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            List<Character> line = new ArrayList<>();
            for (int j = 0; j < input.get(i).length(); j++) {
                char c = input.get(i).charAt(j);
                line.add(c);
                if (c == '^') {
                    currRow = i;
                    currCol = j;
                }
            }
            map.add(line);
        }

        // 1
        map = solve(map, currRow, currCol, Direction.UP);
        System.out.println("1: " + countChars(map, 'X'));

        long time = System.currentTimeMillis();
        // 2
        System.out.println("2: " + getAmountOfPossibleLoops(map, currRow, currCol, Direction.UP));
        System.out.println("TAKEN: " + (System.currentTimeMillis() - time));
    }

    private static class State {
         Direction enterDirection;
         int row;
         int col;
        State (Direction enterDirection, int row, int col) {
            this.enterDirection = enterDirection;
            this.row = row;
            this.col = col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col, enterDirection);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof State) {
                State other = (State) obj;
                return other.row == row && other.col == col && other.enterDirection == enterDirection;
            }
            return false;
        }
    }

    private static class Point {
        int row;
        int col;
        Point(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Point point)) return false;
            return row == point.row && col == point.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }
    private static int getAmountOfPossibleLoops(List<List<Character>> map, int startRow, int startCol, Direction currDir) {
        HashMap<Point, Direction> getPossibleBlockers = new HashMap<>();
        int[] shift = currDir.getDirectionalShift();
        int newRow = startRow ;
        int newCol = startCol;
        try {
            while (true) {
                int totalCountOfSameBlockBounces = 0;
                while (map.get(newRow + shift[0]).get(newCol + shift[1]) == '#') {
                    currDir = currDir.getNextDirection();
                    shift = currDir.getDirectionalShift();
                    totalCountOfSameBlockBounces++;
                    if (totalCountOfSameBlockBounces == 4) {
                        return countLoops(map, getPossibleBlockers); // Should not happen, just as an Edge-Case
                    }
                }
                newRow += shift[0];
                newCol += shift[1];
                Point p = new Point(newRow, newCol);
                if (!getPossibleBlockers.containsKey(p)) {
                    getPossibleBlockers.put(p, currDir);
                }
            }
        } catch (Exception ignored) {
            return countLoops(map, getPossibleBlockers);
        }
    }

    private static int countLoops(List<List<Character>> map, HashMap<Point, Direction> states) {
        int totalAmount = 0;
        for (Map.Entry<Point, Direction> entry : states.entrySet()) {
            if (isLoop(map, new State(entry.getValue(), entry.getKey().row, entry.getKey().col))) {
                totalAmount++;
            }
        }
        return totalAmount;
    }
    private static boolean isLoop(List<List<Character>> map, State startingState) {
        int blockRow  = startingState.row;
        int blockColumn = startingState.col;
        Direction enterDirection = startingState.enterDirection;

        char before = map.get(blockRow).get(blockColumn);
        map.get(blockRow).set(blockColumn, '#'); // Set Blocker
        // Go Back one, before the block
        int[] shift = enterDirection.turnDirection().getDirectionalShift();
        int newRow = blockRow + shift[0];
        int newCol = blockColumn + shift[1];
        shift = enterDirection.getDirectionalShift();
        HashSet<State> visitedStates = new HashSet<>();

        try {
            while (true) {

                int totalCountOfSameBlockBounces = 0;
                while (map.get(newRow + shift[0]).get(newCol + shift[1]) == '#') {
                    enterDirection = enterDirection.getNextDirection();
                    shift = enterDirection.getDirectionalShift();
                    totalCountOfSameBlockBounces++;
                    if (totalCountOfSameBlockBounces == 4) {
                        map.get(blockRow).set(blockColumn, before);
                        return false;
                    }
                }
                newRow += shift[0];
                newCol += shift[1];
                State currState = new State(enterDirection.copy(), newRow, newCol);
                if (visitedStates.contains(currState)) {
                    map.get(blockRow).set(blockColumn, before);
                    return true; // Loop found
                }
                visitedStates.add(currState);
            }
        } catch (Exception ignored) {
            map.get(blockRow).set(blockColumn, before);
            return false;
        }
    }



    enum Direction {
        UP {
            @Override
            int[] getDirectionalShift() {
                return new int[] {-1,0};
            }

            @Override
            Direction getNextDirection() {
                return RIGHT;
            }

            @Override
            Direction turnDirection() {
                return DOWN;
            }
        },
        RIGHT {
            @Override
            int[] getDirectionalShift() {
                return new int[] {0,1};
            }
            @Override
            Direction getNextDirection() {
                return DOWN;
            }
            @Override
            Direction turnDirection() {
                return LEFT;
            }
        },
        DOWN {
            @Override
            int[] getDirectionalShift() {
                return new int[] {1,0};
            }
            @Override
            Direction getNextDirection() {
                return LEFT;
            }
            @Override
            Direction turnDirection() {
                return UP;
            }
        },
        LEFT {
            @Override
            int[] getDirectionalShift() {
                return new int[] {0,-1};
            }
            @Override
            Direction getNextDirection() {
                return UP;
            }
            @Override
            Direction turnDirection() {
                return RIGHT;
            }
        };

        public Direction copy() {
            return switch (this) {
                case UP -> UP;
                case RIGHT -> RIGHT;
                case DOWN -> DOWN;
                case LEFT -> LEFT;
            };
        }
        abstract Direction turnDirection();
        abstract Direction getNextDirection();
        abstract int[] getDirectionalShift();
    }

    private static List<List<Character>> solve(List<List<Character>> map, int currRow, int currCol, Direction currDir) {
        map.get(currRow).set(currCol, 'X');
        int[] shift = currDir.getDirectionalShift();
        int newRow = currRow + shift[0];
        int newCol = currCol + shift[1];
        try {
            if (map.get(newRow).get(newCol) == '#') {
                currDir = currDir.getNextDirection();
                return solve(map, currRow, currCol, currDir);
            }
        } catch (Exception ignored) {
            return map;
        }
        return solve(map, newRow, newCol, currDir);
    }


    private static int countChars(List<List<Character>> map, char c) {
        int count = 0;
        for (List<Character> line : map) {
            for (char ch : line) {
                if (ch == c) {
                    count++;
                }
            }
        }
        return count;
    }
    private static void printMap(List<List<Character>> map) {
        for (List<Character> line : map) {
            for (char c : line) {
                System.out.print(c);
            }
            System.out.println();
        }
    }
    public static List<String> getInput(String file) {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(file))) {
            return reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read file " + file, e);
        }
    }
}
