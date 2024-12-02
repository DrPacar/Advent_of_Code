import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class One {
    public static void main(String[] args) {

        List<String> input = getInput("resources/input1");
        List<Integer> leftSide = new ArrayList<>();
        List<Integer> rightSide = new ArrayList<>();

        // 1
        input.forEach(c -> {
            String[] splittet = c.split("\\s+");
            leftSide.add(Integer.parseInt(splittet[0]));
            rightSide.add(Integer.parseInt(splittet[1]));
        });
        Collections.sort(leftSide);
        Collections.sort(rightSide);
        int out = 0;
        for (int i = 0; i < leftSide.size(); i++) {
            out += Math.abs(leftSide.get(i) - rightSide.get(i));
        }
        System.out.println(out);

        // 2
        out = 0;
        HashMap<Integer, Integer> map = new HashMap<>();
        for (Integer integer : rightSide) {
            map.put(integer, map.getOrDefault(integer, 0) + 1);
        }
        for (Integer i : leftSide) {
            if (map.containsKey(i)) out += i * map.get(i);
        }
        System.out.println(out);
    }

    public static List<String> getInput(String file) {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(file))) {
            return reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read file " + file, e);
        }
    }
}
