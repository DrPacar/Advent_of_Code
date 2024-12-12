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

        List<Integer> leftSide = new ArrayList<>();
        List<Integer> rightSide = new ArrayList<>();
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
                if (m.matches()) {
                    leftSide.add(Integer.parseInt(m.group(1)));
                    rightSide.add(Integer.parseInt(m.group(2)));
                }
            } else {
                inputs.add(Stream.of(s.split(",")).map(Integer::parseInt).toList());
            }
        }

        List<List<Integer>> wrongOnes = new ArrayList<>();
        // < 6342 = 5732
        // 1
        int totalSum = 0;
        for (List<Integer> list : inputs) {

            boolean legal = true;
            for (int i = 0; i < leftSide.size(); i++) {
                if (!legalOrdering(list, leftSide.get(i), rightSide.get(i))) {
                    legal = false;
                    wrongOnes.add(list);
                    break;
                }
            }
            if (legal) {
                totalSum += list.get(list.size()/2);
            }
        }
        System.out.println("1: "+ totalSum);

        // 2
        totalSum = 0;
        for (List<Integer> list : wrongOnes) {
            List<Integer> sorted = sortList(leftSide, rightSide, new ArrayList<>(list));
            totalSum += sorted.get(sorted.size()/2);
        }
        System.out.println("2: " + totalSum);
    }

    private static List<Integer> sortList(List<Integer> leftSide, List<Integer> rightSide, List<Integer> line) {
        line.sort((a,b) -> {
            for (int i = 0; i < leftSide.size(); i++) {
                if (leftSide.get(i) == a) {
                    if (rightSide.get(i) == b) {
                        return -1;
                    }
                } else if (leftSide.get(i) == b) {
                    if (rightSide.get(i) == a) {
                        return 1;
                    }
                }
            }
            return 0;
        });
        return line;

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
