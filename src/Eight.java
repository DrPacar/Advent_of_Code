import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Eight {
    public static void main(String[] args) {
        List<String> input = getInput("resources/input8");
        List<List<Point>> map = new ArrayList<>();

        HashMap<Point, Character> pointToAntennas = new HashMap<>();
        HashMap<Character, List<Point>> antennasToPoint = new HashMap<>();
        for (int row = 0; row < input.size(); row++) {
            String s = input.get(row);
            List<Point> line = new ArrayList<>();
            for (int col = 0; col < s.length(); col++) {
                char c = s.charAt(col);
                Point p = new Point(row, col, c);
                line.add(p);
                if (c != '.') {
                    pointToAntennas.put(p, c);
                    if (antennasToPoint.containsKey(c)) {
                        antennasToPoint.get(c).add(p);
                    } else {
                        antennasToPoint.put(c, new ArrayList<>(List.of(p)));
                    }
                }
            }
            map.add(line);
        }

        // 1
        List<List<Point>> map2 = getAntiNodes1(map, antennasToPoint);
        System.out.println("1: " + countCharOccurences(map2, '#'));
        //2
        List<List<Point>> map3 = getAntiNodes2(map, antennasToPoint);
        System.out.println("2: " + countCharOccurences(map3, '#'));

    }

    private static int countCharOccurences(List<List<Point>> map, char c) {
        int count = 0;
        for (List<Point> line : map) {
            for (Point point : line) {
                if (point.c == c) count++;
            }
        }
        return count;
    }
    private static List<List<Point>> getAntiNodes1(List<List<Point>> map, HashMap<Character, List<Point>> antennas) {
        for (List<Point> line : antennas.values()) {
            for (int i = 0; i < line.size(); i++) {
                for (int j = 0; j < line.size(); j++) {
                    if (i == j) continue;
                    Point one = line.get(i);
                    Point two = line.get(j);
                    int rowDiff = one.x - two.x;
                    int colDiff = one.y - two.y;
                    try {
                        map.get(one.x + rowDiff).get(one.y + colDiff).c = '#';
                    } catch (Exception ignored) {}

                    try {
                        map.get(two.x - rowDiff).get(two.y - colDiff).c = '#';
                    } catch (Exception ignored) {}
                }
            }
        }
        return map;
    }
    private static List<List<Point>> getAntiNodes2(List<List<Point>> map, HashMap<Character, List<Point>> antennas) {
        for (List<Point> line : antennas.values()) {
            for (int i = 0; i < line.size(); i++) {
                for (int j = 0; j < line.size(); j++) {
                    if (i == j) continue;
                    Point one = line.get(i);
                    Point two = line.get(j);

                    int rowDiff = one.x - two.x;
                    int colDiff = one.y - two.y;
                    try {
                        int currX = one.x;
                        int currY = one.y;
                        while (true) {
                            map.get(currX).get(currY).c = '#';
                            currX += rowDiff;
                            currY += colDiff;
                        }
                    } catch (Exception ignored) {}

                    try {
                        int currX = one.x;
                        int currY = one.y;
                        while (true) {
                            map.get(currX).get(currY).c = '#';
                            currX -= rowDiff;
                            currY -= colDiff;
                        }
                    } catch (Exception ignored) {}

                }
            }
        }
        return map;
    }
    static class Point {
        int x;
        int y;
        char c;
        Point(int x, int y, char c) {
            this.x = x;
            this.y = y;
            this.c = c;
        }

        @Override
        public String toString() {
            return c+ "";
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
