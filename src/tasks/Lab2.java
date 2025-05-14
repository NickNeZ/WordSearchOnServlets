package tasks;

import Utils.WordUtils;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Lab2 {
    public static void main(String[] args) {

        String[] firstFileWord = new String[2];
        String[] secondFileWord = new String[2];

        inputHandler(firstFileWord);
        inputHandler(secondFileWord);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            WordUtils wordUtils = new WordUtils();
            wordUtils.CalculateFrequency(firstFileWord[0]);
            wordUtils.printWordFrequency(firstFileWord[1], firstFileWord[0]);
        });
        executor.submit(() -> {
            WordUtils wordUtils = new WordUtils();
            wordUtils.CalculateFrequency(secondFileWord[0]);
            wordUtils.printWordFrequency(secondFileWord[1], secondFileWord[0]);
        });

        executor.shutdown();
    }

    public static String[] inputHandler(String[] inputStings){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите название файла: ");
        String fileName = scanner.nextLine();
        fileName = fileName + ".txt";

        System.out.print("Введите слово для поиска по файлу: ");
        String searchWord = scanner.nextLine();
        searchWord = searchWord.toLowerCase();

        inputStings[0] = fileName;
        inputStings[1] = searchWord;

        return inputStings;
    }
}
