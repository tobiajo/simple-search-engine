package xyz.johansson.simplesearchengine;

import com.google.inject.Singleton;

import java.util.*;

/**
 * Handles indexing for SearchEngine.
 */
@Singleton
public class Indexer {

    // inverted index: word -> {titles}
    private Map<String, Set<String>> word2Titles = new HashMap<>();

    // title -> {word -> raw frequency}
    private Map<String, Map<String, Double>> title2WordFrequency = new HashMap<>();

    /**
     * Empty constructor.
     */
    public Indexer() {

    }

    /**
     * Adds a document to the search engine indexer. Populates an inverted
     * index and store the documents word frequency.
     *
     * @param title   document title or path
     * @param content content of added document
     * @return true if document is added i.e. title not already used or invalid
     */
    public boolean addDocument(String title, String content) {
        if (title2WordFrequency.containsKey(title) || title.isEmpty()) {
            return false;
        }

        Map<String, Integer> wordCountMap = new HashMap<>();
        int totalNumWords = 0;

        StringTokenizer st = new StringTokenizer(content);
        while (st.hasMoreTokens()) {
            String lowerCaseWord = st.nextToken().toLowerCase();
            totalNumWords++;

            Integer numWords = wordCountMap.get(lowerCaseWord);
            if (numWords == null) {
                numWords = 0;
            }
            wordCountMap.put(lowerCaseWord, numWords + 1);

            // titles for documents that contains word
            Set<String> titleSet = word2Titles.get(lowerCaseWord);
            if (titleSet == null) {
                titleSet = new HashSet<>();
                word2Titles.put(lowerCaseWord, titleSet);
            }
            titleSet.add(title);
        }

        // in the case that the Indexer and SearchEngine isn't running on the
        // same JVM: if this logic is not on the Indexer, we need to transfer
        // very much more data to the SearchEngine to extract the frequency.
        Map<String, Double> wordFrequency = new HashMap<>();
        for (String word : wordCountMap.keySet()) {
            wordFrequency.put(word, (double) wordCountMap.get(word) / totalNumWords);
        }
        title2WordFrequency.put(title, wordFrequency);

        return true;
    }

    /**
     * Get the the titles for all documents that contains word.
     *
     * @param word sought word in possible documents, case insensitive
     * @return titles for documents that contains word
     */
    public Set<String> getTitleSet(String word) {
        String lowerCaseWord = word.toLowerCase();
        Set<String> titleSet = word2Titles.get(lowerCaseWord);
        if (titleSet == null) {
            titleSet = new HashSet<>();
        }
        return titleSet;
    }

    /**
     * Gives the frequency for a word in a specific document.
     *
     * @param title title for the document, case insensitive
     * @param word  word for the frequency
     * @return raw frequency for word in document, or null not available
     */
    public Double getWordFrequency(String title, String word) {
        String lowerCaseWord = word.toLowerCase();
        Map<String, Double> wordFrequency = title2WordFrequency.get(title);
        if (wordFrequency == null) {
            return null;
        }
        return wordFrequency.get(lowerCaseWord);
    }

    /**
     * Returns the total number of document titles.
     *
     * @return total number of document titles
     */
    public int getTotalNumTitles() {
        return title2WordFrequency.size();
    }
}
