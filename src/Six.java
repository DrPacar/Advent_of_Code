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

        map = solve(map, currRow, currCol, Direction.UP);
        //printMap(map);
        System.out.println("1: " + countChars(map, 'X'));

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
        };

        abstract Direction getNextDirection();
        abstract int[] getDirectionalShift();
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
