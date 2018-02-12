package com.pingan.cc.channel.o2o.searcher.domain;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.search.DoubleValuesSource;
import org.apache.lucene.search.Sort;
import org.apache.lucene.spatial.SpatialStrategy;
import org.apache.lucene.spatial.prefix.RecursivePrefixTreeStrategy;
import org.apache.lucene.spatial.prefix.tree.GeohashPrefixTree;
import org.apache.lucene.spatial.prefix.tree.SpatialPrefixTree;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.distance.DistanceUtils;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.impl.PointImpl;

public class SpatialIndexer {
    private static final SpatialContext CTX = SpatialContext.GEO;
    private static final SpatialPrefixTree GRID = new GeohashPrefixTree(CTX, 11);
    private static final SpatialStrategy STRATEGY = new RecursivePrefixTreeStrategy(GRID, "location");

    public void createIndex(Document doc, Location location) {
        Point point = CTX.getShapeFactory().pointXY(location.getLat(), location.getLng());
        for (Field f : STRATEGY.createIndexableFields(point)) {
            doc.add(f);
            doc.add(new StoredField(STRATEGY.getFieldName(), point.getX() + " " + point.getY()));
        }
    }

    public Sort getSortField(Location location) {
        Point point = CTX.getShapeFactory().pointXY(location.getLat(), location.getLng());
        DoubleValuesSource valueSource = STRATEGY.makeDistanceValueSource(point, DistanceUtils.DEG_TO_KM);
        return new Sort(valueSource.getSortField(true));
    }

    public double calculateDistance(double x1, double y1, double x2, double y2) {
        double doc1DistDEG = CTX.calcDistance(new PointImpl(x1, y1, CTX), x2, y2);
        return DistanceUtils.degrees2Dist(doc1DistDEG, DistanceUtils.EARTH_MEAN_RADIUS_KM);
    }
}
