import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean fileLoaded = false;
        Dictionary dc = null;

        do {
            try {
                System.out.print("Enter file path: ");
                String filePath = sc.nextLine();

                File file = new File(filePath);
                dc = new Dictionary(file);
                System.out.println("Dictionary loaded successfully");

                fileLoaded = true;
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } while (!fileLoaded);

        String choice;
        do {
            System.out.println();
            System.out.println("Options: ");
            System.out.println("Find word (f)");
            System.out.println("Add word (a)");
            System.out.println("Delete word (d)");
            System.out.println("Find similar words (r)");
            System.out.println("Save file (s)");
            System.out.println("Quit (q)");
            System.out.print("Choose an option: ");
            choice = sc.nextLine().toLowerCase();

            switch (choice) {
                case "f" -> {
                    System.out.print("Check for word: ");
                    String word = sc.nextLine();
                    if (dc.findWord(word)) System.out.println("Word found");
                    else System.out.println("Word not found");
                }
                case "a" -> {
                    System.out.print("Add word: ");
                    String word = sc.nextLine();
                    try {
                        dc.addWord(word);
                        System.out.print("Word added successfully");
                        System.out.println();
                    } catch (WordAlreadyExistsException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case "d" -> {
                    System.out.println("Delete word: ");
                    String word = sc.nextLine();
                    try {
                        dc.deleteWord(word);
                        System.out.print("Word deleted successfully");
                        System.out.println();
                    } catch (WordNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case "r" -> {
                    System.out.print("Search for similar words: ");
                    String word = sc.nextLine();
                    String similarWords = Arrays.toString(dc.findSimilar(word));
                    System.out.println(similarWords.substring(1, similarWords.length() - 1));
                }
                case "s", "q" -> {
                    System.out.print("Enter filename: ");
                    String savingFileName = sc.nextLine();
                    if (dc.saveToFile(savingFileName)) {
                        System.out.println("File saved succesfully");
                    } else {
                        System.out.println("An error occurred, try again");
                    }
                }
                default -> System.out.println("Invalid option");
            }
        } while (!choice.equals("q"));
        sc.close();
    }
}