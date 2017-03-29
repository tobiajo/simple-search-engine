package xyz.johansson.simplesearchengine;


import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class TestIndexer {

    private static final double DELTA = 1 / 1000000;

    @Test
    public void testAddDocument() {
        Indexer indexer = new Indexer();
        indexer.addDocument("Document 1", "the brown fox jumped over the brown dog");
        indexer.addDocument("Document 2", "the lazy brown dog sat in the corner");
        indexer.addDocument("Document 3", "the red fox bit the lazy dog");

        Set<String> titleSetBrown = indexer.getTitleSet("brown");
        Assert.assertEquals(2, titleSetBrown.size());
        Assert.assertTrue(titleSetBrown.contains("Document 1"));
        Assert.assertTrue(titleSetBrown.contains("Document 2"));
        Assert.assertEquals((double) 2 / 8, indexer.getWordFrequency("Document 1", "brown"), DELTA);
        Assert.assertEquals((double) 1 / 8, indexer.getWordFrequency("Document 2", "brown"), DELTA);
        Assert.assertEquals(null, indexer.getWordFrequency("Document 3", "brown"));


        Set<String> titleSetFox = indexer.getTitleSet("fox");
        Assert.assertEquals(2, titleSetFox.size());
        Assert.assertTrue(titleSetFox.contains("Document 1"));
        Assert.assertTrue(titleSetFox.contains("Document 3"));
        Assert.assertEquals((double) 1 / 8, indexer.getWordFrequency("Document 1", "fox"), DELTA);
        Assert.assertEquals(null, indexer.getWordFrequency("Document 2", "fox"));
        Assert.assertEquals((double) 1 / 7, indexer.getWordFrequency("Document 3", "fox"), DELTA);

        Assert.assertEquals(3, indexer.getTotalNumTitles());
    }
}
