import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Nine {
    public static void main(String[] args) {

        List<String> input = getInput("resources/input9");
        String line = input.getFirst();

        LinkedList<Block> blocks = getBlocks(line);
        LinkedList<Block> refactored = refactor(blocks);
        long id = 0;
        long totalSum = 0;
        for (Block block : refactored) {
            if (block.id != -1) {
                for (int i = 0; i < block.amount; i++) {
                    totalSum += id * block.id;
                    id++;
                }
            } else id+=block.amount;
        }

        System.out.println(totalSum);
    }

    private static String printToDebugString(LinkedList<Block> blockLinkedList) {
        StringBuilder sb = new StringBuilder();
        for (Block block : blockLinkedList) {
            sb.append(block.toDebugString());
        }
        return sb.toString();
    }
    private static LinkedList<Block> refactor(LinkedList<Block> input) {
        int currFirstFree = 0;
        for (int i = input.size()-1; i >= currFirstFree ; i--) {
            Block currBlock = input.get(i);
            if (currBlock.id != -1) {
                // Found Block to analyze
                boolean foundFirstFree = false;
                for (int j = currFirstFree; j < i; j++) {
                    Block freeBlock = input.get(j);
                    if (freeBlock.id != -1) continue;
                    if (!foundFirstFree) {
                        foundFirstFree = true;
                        currFirstFree = j;
                    }
                    if (freeBlock.amount < currBlock.amount) continue;
                    System.out.println("SWAPPED: " + currBlock + " with " + freeBlock + " " + j);
                    // Free Block found
                    int newAmountCreated = freeBlock.amount - currBlock.amount;
                    // Insert at Free Space
                    input.set(j, currBlock);
                    if (newAmountCreated != 0) {
                        input.add(j+1, new Block(-1, newAmountCreated));
                        i++;
                        j++;
                    }
                    // Remove old Block
                    List<Integer> removeBlocks = new ArrayList<>();
                    int totalFreeAmount = currBlock.amount;

                    try {
                        Block rightBlock = input.get(i+1);
                        if (rightBlock.id == -1) {
                            removeBlocks.add(i+1);
                            totalFreeAmount += rightBlock.amount;
                        }
                    } catch (Exception ignored) {}

                    try {
                        Block leftBlock = input.get(i-1);
                        if (leftBlock.id == -1) {
                            removeBlocks.add(i-1);
                            totalFreeAmount += leftBlock.amount;
                        }
                    } catch (Exception ignored) {}

                    input.set(i, new Block(-1, totalFreeAmount));
                    removeBlocks.forEach(item -> input.remove((int) item));

                    break;
                }
            }
        }
        System.out.println("Refactored: " + input);
        return input;
    }

    private static LinkedList<Block> getBlocks(String input) {
        LinkedList<Block> blocks = new LinkedList<>();
        int id = 0;
        boolean readingFile = true;
        for (char c : input.toCharArray()) {
            if (Integer.parseInt(c + "") != 0) {
                if (readingFile) {
                    blocks.add(new Block(id, Integer.parseInt(c + "")));
                    id++;
                } else {
                    if (!blocks.isEmpty() && blocks.getLast().id == -1) {
                        blocks.set(blocks.size() - 1, new Block(-1,  blocks.getLast().amount + Integer.parseInt(c + "")));
                    } else {
                        blocks.add(new Block(-1, Integer.parseInt(c + "")));
                    }
                }
            }
            readingFile = !readingFile;
        }
        return blocks;
    }

    static class Block {
        int id = 0;
        int amount = 0;
        Block(int id, int amount) {
            this.id = id;
            this.amount = amount;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Block block = (Block) o;
            return id == block.id && amount == block.amount;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, amount);
        }

        public String toDebugString() {
            String output = "";
            for (int i = 0; i < amount; i++) {
                if (id == -1) output += ".";
                else output += id;
            }
            return output;
        }
        @Override
        public String toString() {
            if (id == -1) {
                return "FREE-" +amount;
            } else {
                return id + "-" + amount;
            }
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
