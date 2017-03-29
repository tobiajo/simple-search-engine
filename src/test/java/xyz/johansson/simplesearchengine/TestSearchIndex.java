package xyz.johansson.simplesearchengine;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TestSearchIndex {

    @Test
    public void testResultOrder() {
        Indexer indexer = new Indexer();
        SearchEngine searchEngine = new SearchEngine(indexer);
        indexer.addDocument("Document 1", "the brown fox jumped over the brown dog");
        indexer.addDocument("Document 2", "the lazy brown dog sat in the corner");
        indexer.addDocument("Document 3", "the red fox bit the lazy dog");

        List<String> resultBrown = searchEngine.search("brown");
        Assert.assertEquals(2, resultBrown.size());
        Assert.assertEquals("Document 1", resultBrown.get(0));
        Assert.assertEquals("Document 2", resultBrown.get(1));

        List<String> resultFox = searchEngine.search("fox");
        Assert.assertEquals(2, resultFox.size());
        Assert.assertEquals("Document 3", resultFox.get(0)); // has higher raw frequency
        Assert.assertEquals("Document 1", resultFox.get(1));
    }
}
