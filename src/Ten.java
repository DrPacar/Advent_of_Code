import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Ten {
    public static void main(String[] args) {
        List<String> input = getInput("resources/input10");
        List<List<Integer>> map = new ArrayList<>();
        List<Point> startingPoints = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            String s = input.get(i);
            List<Integer> list = new ArrayList<>();
            for (int j = 0; j < s.length(); j++) {
                char c = s.charAt(j);
                if (c == '0') {
                    Point p = new Point(i, j);
                    // Starting-Point
                    startingPoints.add(p);
                }
                list.add(Integer.parseInt(String.valueOf(c)));
            }
            map.add(list);
        }

        int totalCount = 0;
        for (Point point : startingPoints) {
            int yeah = iterateThroughMap(map, point, new int[]{1, 0, -1, 0}, new int[]{0, 1, 0, -1});
            System.out.println("STARTING AT : "+ point + "  " + yeah);
            totalCount += yeah;
        }

        System.out.println("1: " + totalCount);

    }

    private static int iterateThroughMap(List<List<Integer>> map, Point currPoint, int[] xDir, int[] yDir) {
        int valAtCurrPoint = map.get(currPoint.x).get(currPoint.y);
        System.out.println(valAtCurrPoint);
        if (valAtCurrPoint == 9) return 1;
        int totalCount = 0;
        for (int i = 0; i < xDir.length; i++) {
            int x = currPoint.x + xDir[i];
            int y = currPoint.y + yDir[i];

            try {
                if (map.get(x).get(y) == valAtCurrPoint + 1) {
                    totalCount += iterateThroughMap(map, new Point(x, y), xDir, yDir);
                }
            } catch (Exception ignored) {}
        }
        return totalCount;
    }

    static class Point {
        int x, y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" +x+ "," + y + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }
        @Override
        public int hashCode() {
            return Objects.hash(x, y);
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
