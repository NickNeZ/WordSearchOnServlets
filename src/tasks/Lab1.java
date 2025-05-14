package tasks;

import Utils.WordUtils;
import java.util.Scanner;

public class Lab1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите название файла: ");
        String filePath = scanner.nextLine();
        filePath = filePath + ".txt";

        System.out.print("Введите слово для поиска по файлу: ");
        String searchWord = scanner.nextLine();
        searchWord = searchWord.toLowerCase();

        // Создание экземпляра WordUtils
        WordUtils wordUtils = new WordUtils();

        // Вызов метода CalculateFrequency, передавая имя файла
        wordUtils.CalculateFrequency(filePath);

        wordUtils.printWordFrequency(searchWord, filePath);
    }
}
