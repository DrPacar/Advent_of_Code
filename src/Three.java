import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Three {
    public static void main(String[] args) {
        List<String> input = getInput("resources/input3");
        // 1
        Pattern p = Pattern.compile("mul\\((\\d+),(\\d+)\\)");
        int totalSum = 0;
        for (String s : input) {
            Matcher m = p.matcher(s);
            while (m.find()) {
                int num1 = Integer.parseInt(m.group(1));
                int num2 = Integer.parseInt(m.group(2));
                totalSum += num1 * num2;
            }
        }
        System.out.println(totalSum);
        // 2
        System.out.println("do dont start first second");
        p = Pattern.compile("((do\\(\\))|(don't\\(\\)))|(mul\\((\\d+),(\\d+)\\))");
        totalSum = 0;
        boolean allowed = true;
        for (String s : input) {
            Matcher m = p.matcher(s);
            while (m.find()) {
                if (m.group(1) != null) {
                    System.out.println(m.group(1));
                    allowed = m.group(1).equals("do()");
                    continue;
                }
                if (!allowed) continue;
                int num1 = Integer.parseInt(m.group(5));
                int num2 = Integer.parseInt(m.group(6));
                totalSum += num1 * num2;
            }
        }
        System.out.println(totalSum);
    }

    public static List<String> getInput(String file) {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(file))) {
            return reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read file " + file, e);
        }
    }
}
