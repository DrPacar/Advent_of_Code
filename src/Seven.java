import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Seven {
    public static void main(String[] args) {
        List<String> input = getInput("resources/input7");
        Pattern p = Pattern.compile("(\\d+): (.*)");
        List<List<Long>> itemList = new ArrayList<>();
        List<Long> targetList = new ArrayList<>();
        for (String s : input) {
            Matcher m = p.matcher(s);
            if (m.matches()) {
                long target = Long.parseLong(m.group(1));
                targetList.add(target);
                List<Long> items = Stream.of(m.group(2).split(" ")).map(Long::parseLong).toList();
                itemList.add(items);
            }
        }

        // 1
        long totalCount = 0;
        for (int i = 0; i < targetList.size(); i++) {
            long target = targetList.get(i);
            List<Long> items = itemList.get(i);
            if (isValid(target, items, false)) {
                totalCount += target;
            }
        }
        System.out.println("1: " + totalCount);

        // 2
        totalCount = 0;
        for (int i = 0; i < targetList.size(); i++) {
            long target = targetList.get(i);
            List<Long> items = itemList.get(i);
            if (isValid(target, items, true)) {
                totalCount += target;
            }
        }
        System.out.println("2: " + totalCount);
    }

    private static boolean isValid(long target, List<Long> items, boolean includeConcat) {
        if (items.size() == 1) {
            return items.getFirst() == target;
        }
        List<Long> multi = new ArrayList<>(items);
        List<Long> added = new ArrayList<>(items);
        List<Long> concat = new ArrayList<>(items);
        long first = items.getFirst();
        long second = items.get(1);
        multi.removeFirst();
        added.removeFirst();
        concat.removeFirst();
        multi.set(0, first * second);
        added.set(0, first + second);
        concat.set(0, Long.parseLong(first + "" + second));
        if (isValid(target, multi, includeConcat)) return true;
        if (isValid(target, added, includeConcat)) return true;
        if (includeConcat && isValid(target, concat, includeConcat)) return true;
        return false;
    }
    public static List<String> getInput(String file) {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(file))) {
            return reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read file " + file, e);
        }
    }
}
