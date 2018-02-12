package com.pingan.cc.channel.o2o.searcher.domain;

import java.io.FileInputStream;
import java.util.Properties;

public final class Configs {
    private static final String LUCENE_INDEX_LOCATION_KEY = "lucene.index.location";
    private static final String CONFIG_FILE_NAME = "config.properties";
    private static final Properties properties = new Properties();

    static {
        try {
            FileInputStream in = new FileInputStream(Configs.class.getClassLoader().getResource(CONFIG_FILE_NAME).getFile());
            properties.load(in);
        } catch (Exception ex) {
            throw new IllegalStateException("can not read config file.");
        }
    }

    public static String get(final String key) {
        return properties.getProperty(key);
    }

    public static String getIndexDir() {
        return get(LUCENE_INDEX_LOCATION_KEY);
    }
}
