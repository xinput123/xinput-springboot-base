package com.xinput.bootbase.util;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * @author <a href="mailto:xinput.xx@gmail.com">xinput</a>
 * @Date: 2019-09-27 11:21
 */
public class FileHelper {

    public static <T> T readFileBean(String fileName, Class<T> clazz) throws IOException {
        String demoTaskString = Files.asCharSource(new File(fileName), Charsets.UTF_8).read();
        return JsonUtils.toBean(demoTaskString, clazz);
    }

    public static <T> List<T> readFileList(String fileName, Class<T> clazz) throws IOException {
        String demoTaskString = Files.asCharSource(new File(fileName), Charsets.UTF_8).read();
        return JsonUtils.toList(demoTaskString, clazz);
    }

    /**
     * 将csv文件内容转为实体对象
     *
     * @param csvText
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> readCsv(String csvText, Class<T> clazz) {
        return readCsv(csvText, 0, clazz);
    }

    /**
     * @param csvText
     * @param skipRowNum
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> readCsv(String csvText, int skipRowNum, Class<T> clazz) {
        BeanListProcessor<T> rowProcessor = new BeanListProcessor(clazz);
        CsvParserSettings parserSettings = new CsvParserSettings();
        parserSettings.setProcessor(rowProcessor);
        parserSettings.getFormat().setLineSeparator("\n");
        parserSettings.getFormat().setDelimiter('\t');
        parserSettings.setNumberOfRowsToSkip(skipRowNum);

        CsvParser parser = new CsvParser(parserSettings);
        parser.parse(new StringReader(csvText));

        return rowProcessor.getBeans();
    }
}
