import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Dictionary {
    private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz ".toCharArray();
    private static final int NUMBER_OF_LETTERS = ALPHABET.length;

    // Class that defines each node in the tree. Each node has a letter corresponding to it, a boolean value to
    // identify if it is a word, and an array of next pointing to nodes that act as possible next letters in a word
    private static class DictNode {
        char letter;
        boolean word;
        DictNode[] next;
        public DictNode() {
            this.next = new DictNode[NUMBER_OF_LETTERS];
        }

        public DictNode(char letter) {
            this.letter = letter;
            this.word = false;
            this.next = new DictNode[NUMBER_OF_LETTERS];
        }
    }

    // The root node of the tree
    private final DictNode root;

    // Default constructor
    public Dictionary() {
        this.root = new DictNode();
    }

    // Constructor that initiates the dictionary with one word s
    public Dictionary(String s) {
        this.root = new DictNode();
        try {
            addWord(s);
        } catch (WordAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    // Loads all words of a file into the dictionary
    public Dictionary(File f) throws FileNotFoundException {
        Scanner scanner = new Scanner(f);
        this.root = new DictNode();
        while (scanner.hasNextLine()) {
            try {
                addWord(scanner.nextLine());
            } catch (WordAlreadyExistsException ignored) {}
        }
        scanner.close();
    }

    // Method to add a word to the dictionary
    public void addWord(String s) throws WordAlreadyExistsException {
        DictNode current = root;
        // Iterating to through nodes to get to the desired node to add
        for (char c: s.toCharArray()) {
            if (current.next[getIndex(c)] == null) { // If a child letter doesn't exist, we add it
                current.next[getIndex(c)] = new DictNode(c);
            }
            current = current.next[getIndex(c)];
        }
        if (current.word) {
            throw new WordAlreadyExistsException("Word " + s + " already exists");
        } else {
            current.word = true;
        }
    }

    // Method that deletes a word
    public void deleteWord(String s) throws WordNotFoundException {
        DictNode current = root;
        for (int treeIter = 0; treeIter < s.length(); treeIter++) {
            if (current.next[getIndex(s.charAt(treeIter))] == null) {
                throw new WordNotFoundException(s + " not found"); // We throw an exception if one of the letters cant be found
            }
            current = current.next[getIndex(s.charAt(treeIter))];
        }

        if (!current.word) {
            throw new WordNotFoundException(s + " not found"); // Word is not in the dictionary
        }
        current.word = false; // Delete the word
    }

    // Method that returns true if a word is in the dictionary otherwise false
    public boolean findWord(String s) {
        DictNode current = root;
        for (int treeIter = 0; treeIter < s.length(); treeIter++) {
            if (current.next[getIndex(s.charAt(treeIter))] == null) {
                return false;
            }
            current = current.next[getIndex(s.charAt(treeIter))];
        }
        return current.word;
    }

    // Method that finds all similar words, a similar word is a word that has a 1 letter difference
    public String[] findSimilar(String s) {
        SLL<String> similar = new SLL<>();
        String word;
        for (int i=0; i< 2 * s.length() + 1; i++) {
            int index = i / 2;
            for (char letter: ALPHABET) { // Iterating over all possible letter combinations
                if (letter != ' ' && i % 2 == 0) {
                    word = s.substring(0, index) + letter + s.substring(index);
                } else if (letter != ' ') {
                    word = s.substring(0, index) + letter + s.substring(index + 1);
                }
                else if (i % 2 == 1) {
                    word = s.substring(0, index) + s.substring(index + 1);
                } else {
                    continue;
                }
                if (findWord(word) && !word.equals(s)) {
                    similar.addToTail(word);
                }
            }
        }
        return similar.toStringArray();
    }

    // Method that saves the dictionary to a file in alphabetical order using breadth first
    public boolean saveToFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                boolean fileCreated = file.createNewFile();
                if (!fileCreated) {
                    throw new RuntimeException("File was not created successfully");
                }
            }
            PrintWriter pw = new PrintWriter(file);
            bfsWrite(pw, root, "");
            pw.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Recursively called method that writes to the file in a breadth first method
    private void bfsWrite(PrintWriter pw, DictNode node, String word) {
        if (node.word) {
            pw.println(word);
        }
        for (DictNode child: node.next) {
            if (child != null) {
                bfsWrite(pw, child, word + child.letter);
            }
        }
    }

    // Method used to get an index from the letter (a=0, b=1, c=2, ...)
    private int getIndex(char letter) {
        return letter - 'a';
    }
}
