package xyz.johansson.simplesearchengine;

public class Application {

    /**
     * Main method.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        Indexer indexer = new Indexer();
        SearchEngine searchEngine = new SearchEngine(indexer);
        new CLI(indexer, searchEngine).run();
    }
}
