import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Four {
    public static void main(String[] args) {
        List<String> input = getInput("resources/input4");
        List<List<Character>> map = new ArrayList<>();
        for (String s : input) {
            map.add(s.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
        }
        // 1
        int totalCount = 0;
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.getFirst().size(); j++) {
                if (map.get(i).get(j) == 'X') {
                    totalCount +=
                            traverse(map, i, j, new int[]{-1}, new int[]{0}, XMAS_State.X) +
                            traverse(map, i, j, new int[]{1}, new int[]{0}, XMAS_State.X)  +
                            traverse(map, i, j, new int[]{0}, new int[]{-1}, XMAS_State.X) +
                            traverse(map, i, j, new int[]{0}, new int[]{1}, XMAS_State.X)  +
                            traverse(map, i, j, new int[]{1}, new int[]{-1}, XMAS_State.X) +
                            traverse(map, i, j, new int[]{-1}, new int[]{1}, XMAS_State.X) +
                            traverse(map, i, j, new int[]{1}, new int[]{1}, XMAS_State.X)  +
                            traverse(map, i, j, new int[]{-1}, new int[]{-1}, XMAS_State.X);
                    System.out.println("P("+ i + "|" + j + ")= " + totalCount);
                }
            }
        }
        System.out.println(totalCount);

        // 2
        HashSet<List<List<Character>>> legalFormations = getLegalFormations();
        totalCount = 0;
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.getFirst().size(); j++) {
                if (map.get(i).get(j) == 'A') {
                    try {
                        if (legalFormations.contains(remove3times3Edges(cutMap(map, i-1, j-1, 3, 3)))) {
                            totalCount++;
                        }
                    } catch (Exception ignored) {}
                }
            }
        }
        System.out.println(totalCount);
    }
    private static HashSet<List<List<Character>>> getLegalFormations() {

        List<List<Character>> formation1 = new ArrayList<>();
        formation1.add(List.of('M', '-', 'M'));
        formation1.add(List.of('-', 'A', '-'));
        formation1.add(List.of('S', '-', 'S'));

        List<List<Character>> formation2 = new ArrayList<>();
        formation2.add(List.of('M', '-', 'S'));
        formation2.add(List.of('-', 'A', '-'));
        formation2.add(List.of('M', '-', 'S'));

        List<List<Character>> formation3 = new ArrayList<>();
        formation3.add(List.of('S', '-', 'S'));
        formation3.add(List.of('-', 'A', '-'));
        formation3.add(List.of('M', '-', 'M'));

        List<List<Character>> formation4 = new ArrayList<>();
        formation4.add(List.of('S', '-', 'M'));
        formation4.add(List.of('-', 'A', '-'));
        formation4.add(List.of('S', '-', 'M'));

        return new HashSet<>(List.of(formation1, formation2, formation3, formation4));
    }
    private static List<List<Character>> remove3times3Edges(List<List<Character>> map) {
        map.getFirst().set(map.get(0).size()/2, '-');
        map.get(1).set(0, '-');
        map.get(1).set(map.get(1).size()-1, '-');
        map.getLast().set(map.get(0).size()/2, '-');
        return map;
    }
    private static List<List<Character>> cutMap(List<List<Character>> map, int cutX, int cutY, int width, int height) {
        List<List<Character>> newMap = new ArrayList<>();
        for (int i = cutX; i < cutX + width; i++) {
            List<Character> row = new ArrayList<>();
            for (int j = cutY; j < cutY + height; j++) {
                row.add(map.get(i).get(j));
            }
            newMap.add(row);
        }
        return newMap;
    }


    private static int traverse(List<List<Character>> map, int currPointX, int currPointY, int[] xJumps, int[] yJumps, XMAS_State state) {
        if (state == XMAS_State.DONE) return 1;
        int totalCount = 0;
        for (int i = 0; i < xJumps.length; i++) {
            int x = currPointX + xJumps[i];
            int y = currPointY + yJumps[i];
            try {
                if (state.getExpectedChar() == null || map.get(x).get(y) == state.getExpectedChar()) {
                    totalCount += traverse(map, x, y, xJumps, yJumps, state.next());
                }
            } catch (Exception ignored) {}
        }
        return totalCount;
    }
    public enum XMAS_State {
        X {
            @Override
            XMAS_State next() {
                return M;
            }
            @Override
            Character getExpectedChar() {
                return 'M';
            }
        },
        M {
            @Override
            XMAS_State next() {
                return A;
            }
            @Override
            Character getExpectedChar() {
                return 'A';
            }
        },
        A {
            @Override
            XMAS_State next() {
                return S;
            }
            @Override
            Character getExpectedChar() {
                return 'S';
            }
        },
        S {
            @Override
            XMAS_State next() {
                return DONE;
            }

            @Override
            Character getExpectedChar() {
                return null;
            }
        },
        DONE {
            @Override
            XMAS_State next() {
                return null;
            }

            @Override
            Character getExpectedChar() {
                return null;
            }
        }

        ;

        abstract XMAS_State next();
        abstract Character getExpectedChar();

    }
    public static List<String> getInput(String file) {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(file))) {
            return reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read file " + file, e);
        }
    }
}
