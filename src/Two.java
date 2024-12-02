import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Two {
    public static void main(String[] args) {
        List<String> input = getInput("resources/input2");
        List<List<Integer>> lines = input.stream().map(i -> Stream.of(i.split("\\s+"))
                                                  .map(Integer::parseInt)
                                                  .collect(Collectors.toList()))
                                                  .toList();
        // 1
        int totalSave = 0;
        for (List<Integer> line : lines) {
           if (isSafe(line)) totalSave ++;
        }
        System.out.println(totalSave);

        // 2
        totalSave = 0;
        LINES:
        for (List<Integer> line : lines) {
            if (isSafe(new ArrayList<>(line))) {
                totalSave ++;
            } else {
                for (int i = 0; i < line.size(); i++) {
                    List<Integer> removedItem = new ArrayList<>(line);
                    removedItem.remove(i);
                    if (isSafe(removedItem)) {
                        totalSave++;
                        continue LINES;
                    }
                }
            }

        }
        System.out.println(totalSave);
    }

    private static boolean isSafe(List<Integer> line) {
            int lastInput = line.get(0);
            if (line.get(1) == lastInput) return false;
            boolean increasing = line.get(1) > line.get(0);
            for (int j = 1; j < line.size(); j++) {
                int i = line.get(j);
                if (    Math.abs(lastInput-i) < 1 ||
                        Math.abs(lastInput-i) > 3 ||
                        increasing != i > lastInput) {
                    return false;
                }
                lastInput = i;
            }
            return true;
    }

    public static List<String> getInput(String file) {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(file))) {
            return reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read file " + file, e);
        }
    }
}
