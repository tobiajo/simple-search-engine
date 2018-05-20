package xyz.johansson.simplesearchengine;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.*;

/**
 * Class that handles search queries.
 */
@Singleton
public class SearchEngine {

    private Indexer indexer;

    /**
     * Constructs the search engine.
     *
     * @param indexer search engine indexer
     */
    @Inject
    public SearchEngine(Indexer indexer) {
        this.indexer = indexer;
    }

    /**
     * Gives the search result sorted by TF-IDF.
     *
     * @param query query to the search engine
     * @return an after relevance sorted list of documents that contains word,
     * or null on invalid query
     */
    public List<String> search(String query) {

        // TODO? query => multi-word query
        // approach: tokenize query, get tfidf for each word and combine "points", but how?
        if (new StringTokenizer(query).countTokens() > 1 || query.isEmpty()) {
            return null;
        }

        List<String> sortedTitles = new ArrayList<>();

        Map<String, Double> title2tfidf = new HashMap<>();
        Set<String> titleSet = indexer.getTitleSet(query);

        for (String title : titleSet) {
            double tf = indexer.getWordFrequency(title, query);
            double idf = Math.log10((double) indexer.getTotalNumTitles() / titleSet.size());
            title2tfidf.put(title, tf * idf); // [raw frequency] * [inverse document frequency]
        }

        title2tfidf.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .forEach(entrySet -> sortedTitles.add(entrySet.getKey()));

        return sortedTitles;
    }
}
