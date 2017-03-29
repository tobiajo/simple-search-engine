package xyz.johansson.simplesearchengine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * Provides a command line user interface for Simple search engine.
 */
public class CLI {

    private Indexer indexer;
    private SearchEngine searchEngine;

    private Scanner scanner;

    /**
     * Constructor, sets up Indexer and SearchEngine.
     */
    public CLI() {
        indexer = new Indexer();
        searchEngine = new SearchEngine(indexer);
    }

    /**
     * Runs the command line interface for Simple search engine.
     */
    public void run() {
        System.out.println("\n" +
                "Welcome to Simple search engine\n" +
                "===============================");

        scanner = new Scanner(System.in);
        mainPrompt();
        scanner.close();
    }

    private void mainPrompt() {
        prompt:
        while (true) {
            System.out.print("\n" +
                    "1. Take in documents\n" +
                    "2. Search for documents\n" +
                    "3. Exit\n" +
                    "> ");
            switch (scanner.nextLine()) {
                case "1":
                    loadPrompt();
                    break;
                case "2":
                    searchPrompt();
                    break;
                case "3":
                    break prompt;
                default:
                    System.out.println("Invalid command!");
                    break;
            }
        }
    }

    private void loadPrompt() {
        prompt:
        while (true) {
            System.out.print("\n" +
                    "1. From command line\n" +
                    "2. From files\n" +
                    "3. Back\n" +
                    "> ");
            switch (scanner.nextLine()) {
                case "1":
                    System.out.print("\nTitle: ");
                    String title = scanner.nextLine();
                    System.out.print("Content: ");
                    String content = scanner.nextLine();
                    addDocument(title, content);
                    break;
                case "2":
                    System.out.print("Path to file: ");
                    try {
                        Path filePath = Paths.get(scanner.nextLine()).toAbsolutePath();
                        StringBuilder sb = new StringBuilder();
                        Files.lines(filePath).forEach(line -> {
                            sb.append(line);
                        });
                        addDocument(filePath.toString(), sb.toString());
                    } catch (IOException e) {
                        System.out.println("Error reading file!");
                    }
                    break;
                case "3":
                    break prompt;
                default:
                    System.out.println("Invalid command!");
                    break;
            }
        }
    }

    private void addDocument(String title, String content) {
        boolean added = indexer.addDocument(title, content);
        if (added) {
            System.out.println("Added successfully: " + title);
        } else {
            System.out.println("Taken or invalid title!");
        }
    }

    private void searchPrompt() {
        System.out.print("\nWrite a query: ");
        String query = scanner.nextLine();
        List<String> documentTitles = searchEngine.search(query);
        if (documentTitles == null) {
            System.out.println("Invalid query!");
            return;
        }
        System.out.println("Document titles:");
        if (documentTitles.size() == 0) {
            System.out.println("(none)");
        } else {
            documentTitles.forEach(System.out::println);
        }
    }

    /**
     * Main method.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        new CLI().run();
    }
}
