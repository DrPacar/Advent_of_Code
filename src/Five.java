import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Five {
    public static void main(String[] args) {
        List<String> input = getInput("resources/input5");
        Map<Integer, Integer> map = new HashMap<>();
        Pattern pages = Pattern.compile("(\\d+)\\|(\\d+)");
        boolean readingPages = true;
        List<List<Integer>> inputs = new ArrayList<>();
        for (String s : input) {
            if (readingPages) {
                if (s.isEmpty()) {
                    readingPages = false;
                    continue;
                }
                Matcher m = pages.matcher(s);
                if (m.matches()) map.put(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
            } else {
                inputs.add(Stream.of(s.split(",")).map(Integer::parseInt).toList());
            }
        }

        // < 6342
        // 1
        int totalSum = 0;
        for (List<Integer> list : inputs) {

            boolean legal = true;
            for (int key : map.keySet()) {
                if (!legalOrdering(list, key, map.get(key))) {
                    legal = false;
                    break;
                }
            }
            if (legal) {
                totalSum += list.get(list.size()/2);
            }
        }
        System.out.println("1: "+ totalSum);
    }

    private static boolean legalOrdering(List<Integer> list, int min, int max) {
        boolean foundMax = false;
        for (int i : list) {
            if (i == min) {
                if (foundMax) return false;
            }
            if (i == max) foundMax = true;
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
