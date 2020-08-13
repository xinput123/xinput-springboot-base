package com.xinput.bootbase.util;

import com.google.common.collect.Maps;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.HttpHeaders;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zanxus
 * @version 1.0.0
 * @date 2017-04-20 下午2:57
 */
public class MimeTypeUtils {

    private static final String DEFAULT_VALUE = "text/plain";

    public static String getContentType(File file) {
        if (file.isDirectory()) {
            return "the target is a directory";
        }

        AutoDetectParser parser = new AutoDetectParser();
        parser.setParsers(Maps.newHashMap());

        Metadata metadata = new Metadata();
        metadata.add(Metadata.RESOURCE_NAME_KEY, file.getName());

        InputStream stream;
        try {
            stream = new FileInputStream(file);
            parser.parse(stream, new DefaultHandler(), metadata, new ParseContext());
            stream.close();
        } catch (TikaException | SAXException | IOException e) {

            return DEFAULT_VALUE;
        }

        return metadata.get(HttpHeaders.CONTENT_TYPE);
    }
}