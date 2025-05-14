package tasks;
import Utils.WordUtils;
import Utils.DatabaseUtils;
import java.util.Map;

public class Lab3 {
    public static void main(String[] args) {

        WordUtils utils = new WordUtils();
        String filename = "text2.txt";

        utils.CalculateFrequency(filename);
        Map<String, Integer> frequencies = WordUtils.getFrequencies();

        DatabaseUtils dbManager = new DatabaseUtils();
        dbManager.insertFrequencies(filename, frequencies);
    
        System.out.println("Слова и их частоты записаны в БД.");

    }
}
