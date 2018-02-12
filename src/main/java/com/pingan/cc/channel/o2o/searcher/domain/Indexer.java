package com.pingan.cc.channel.o2o.searcher.domain;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HEQIAO939 on 2018/2/12.
 */
public class Indexer {
    private static final SpatialIndexer SPATIAL_INDEXER = new SpatialIndexer();

    private IndexWriter createIndexWriter() throws IOException {
        FSDirectory directory = FSDirectory.open(Paths.get(Configs.getIndexDir()));
        return new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()));
    }

    private List<Person> createDummyData() {
        List<Person> result = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Person p = new Person("何桥", "上海", 121.45575 + i * 0.01, 31.249571 + i * 0.01);
            result.add(p);
        }
        return result;
    }


    public void create() throws IOException {
        try (IndexWriter writer = createIndexWriter()) {
            for (Person p : createDummyData()) {
                Document doc = new Document();
                doc.add(new StringField("name", p.getName(), Field.Store.YES));
                doc.add(new StringField("city", p.getCity(), Field.Store.YES));
                SPATIAL_INDEXER.createIndex(doc, p.getLocation());
                writer.addDocument(doc);
            }
            writer.commit();
        }
    }

    public static void main(String[] args) throws IOException {
        new Indexer().create();
    }
}
