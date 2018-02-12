package com.pingan.cc.channel.o2o.searcher.domain;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by HEQIAO939 on 2018/2/12.
 */
public class Searcher {
    private static final SpatialIndexer SPATIAL_INDEXER = new SpatialIndexer();

    private IndexSearcher createIndexSearcher() {
        try {
            FSDirectory directory = FSDirectory.open(Paths.get(Configs.getIndexDir()));
            DirectoryReader reader = DirectoryReader.open(directory);
            return new IndexSearcher(reader);
        } catch (IOException e) {
            throw new IllegalStateException("can not create index searcher.");
        }
    }

    public List search(Location location) throws IOException {
        IndexSearcher searcher = createIndexSearcher();
        TopDocs topdocs = searcher.search(new MatchAllDocsQuery(), 1000, SPATIAL_INDEXER.getSortField(location));
        for (ScoreDoc scoreDoc : topdocs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println(doc.get("city"));
            System.out.println(doc.get("name"));
            String doc1Str = doc.get("location");
            int spaceIdx = doc1Str.indexOf(' ');
            double x = Double.parseDouble(doc1Str.substring(0, spaceIdx));
            double y = Double.parseDouble(doc1Str.substring(spaceIdx + 1));
            System.out.println(SPATIAL_INDEXER.calculateDistance(121.45575, 31.249571, x, y));
        }
        return null;
    }

    public static void main(String[] args) throws IOException, ParseException {
        new Searcher().search(new Location(121.45575, 31.249571));
    }
}
