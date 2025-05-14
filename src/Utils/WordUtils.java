package Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class WordUtils {
    private static final String FILES_DIR = "src/data/textfiles/";
    private static final String BLACKLIST_FILE = FILES_DIR + "blacklist.txt";
    private static final Map<String, Integer> wordFrequencies = new HashMap<>();
    private static final Set<String> blacklistedWords = new HashSet<>();

    static {
        try {
            List<String> lines = Files.readAllLines(Paths.get(BLACKLIST_FILE));
            for (String line : lines) {
                blacklistedWords.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void CalculateFrequency(String filename) {
        wordFrequencies.clear();
        try {
            String content = new String(Files.readAllBytes(Paths.get(FILES_DIR + filename)));
            String[] words = content.toLowerCase().split("[^a-zA-Zа-яА-ЯёЁ]+");

           for (String word : words) {
                    if (word.trim().isEmpty() || blacklistedWords.contains(word)) continue;
                    wordFrequencies.put(word, wordFrequencies.getOrDefault(word, 0) + 1);
           }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printFrequencies() {
        for (Map.Entry<String, Integer> entry : wordFrequencies.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public void printWordFrequency(String word, String filename) {
        if (word == null || word.trim().isEmpty()) {
            System.out.println("Слово не может быть пустым.");
            return;
        }
        String cleanedWord = word.trim().toLowerCase();
        int count = wordFrequencies.getOrDefault(cleanedWord, 0);

        System.out.printf("Слово \"%s\" встречается в файле \"%s\" %d раз(а).\n", word, filename, count);
    }

    public static Map<String, Integer> getFrequencies() {
        return new HashMap<>(wordFrequencies);
    }
}
