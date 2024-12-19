import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Eleven {
    public static void main(String[] args) {
        List<String> input = getInput("resources/input11");
        String inputString = input.getFirst();

        // 1
        LinkedList<Long> stones = transformStones(inputString);
        for (int i = 1; i < 75; i++) {
            System.out.println("YO " + i);
            transformStones(stones);
        }
        System.out.println("2 " + stones.size());

        //198075
    }

    private static LinkedList<Long> transformStones(String stones) {
        LinkedList<Long> output = Arrays.stream(stones.split("\\s+")).map(Long::parseLong).collect(Collectors.toCollection(LinkedList::new));
        return transformStones(output);
    }
    private static LinkedList<Long> transformStones(LinkedList<Long> stones) {
        for (int i = 0; i < stones.size(); i++) {
            long stone = stones.get(i);
            long length = 0;
            long timeSpent = System.currentTimeMillis();
            if (stone == 0) {
                stones.set(i, 1L);
            } else if ((length = getDigitLength(stone)) % 2 == 0) {
                long[] split = splitLongInto2Parts(stone, length);
                stones.set(i, split[0]);
                stones.add(i+1, split[1]);
                i++;
            } else {
                stones.set(i, stone * 2024L);
            }
        }
        return stones;
    }

    private static long getDigitLength(long n) {
        return (long) Math.log10(n) + 1;
    }
    private static long[] splitLongInto2Parts(long n, long length) {
        long[] split = new long[2];
        long first = n / (long) Math.pow(10, length / 2);
        long second = n % (long) Math.pow(10, length / 2);
        split[0] = first;
        split[1] = second;
        return split;
    }
    public static List<String> getInput(String file) {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(file))) {
            return reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read file " + file, e);
        }
    }
}
