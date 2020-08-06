package com.bootbase.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * @Author: xinput
 * json格式化
 */
public class JsonUtils {

    private static final Logger logger = Logs.get();

    private static ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            // 指定时区
            .setTimeZone(TimeZone.getTimeZone("GMT+8:00"))
            // 日期类型字符串处理
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
            .registerModule(
                    // java8日期处理
                    new JavaTimeModule()
                            .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                            .addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                            .addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")))
                            .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                            .addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                            .addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")))
            );

    public static String toJsonString(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toJsonString(Object obj, boolean prettyFormat) {
        if (prettyFormat) {
            try {
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }
        return toJsonString(obj);
    }

    /**
     * 将对象字符串(不是List格式),转化成对象.
     *
     * @param content
     * @param clazz
     * @return
     */

    public static <T> T toBean(String content, Class<T> clazz) {
        if (StringUtils.isNullOrEmpty(content)) {
            return null;
        }

        T t = null;
        try {
            t = mapper.readValue(content, clazz);
        } catch (JsonParseException e) {
            logger.error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return t;
    }

    /**
     * 将对象reader(不是List格式),转化成对象.
     *
     * @param reader
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T toBean(Reader reader, Class<T> clazz) {
        if (reader == null) {
            return null;
        }

        T t = null;
        try {
            t = mapper.readValue(reader, clazz);
        } catch (JsonParseException e) {
            logger.error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return t;
    }

    /**
     * 将对象InputStream(不是List格式),转化成对象.
     *
     * @param stream
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T toBean(InputStream stream, Class<T> clazz) {
        if (stream == null) {
            return null;
        }

        T t = null;
        try {
            t = mapper.readValue(stream, clazz);
        } catch (JsonParseException e) {
            logger.error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return t;
    }

    /**
     * 将List对象字符串,转化成List对象.
     *
     * @param content 字符串内容
     * @param clazz   对象类型 例如 User.class
     * @return
     */
    public static <T> List<T> toList(String content, Class<T> clazz) {
        if (content == null) {
            return null;
        }
        return (List<T>) readValueList(content, ArrayList.class, clazz);
    }

    /**
     * 将List对象字符串,转化成List对象.
     *
     * @param content         字符串内容
     * @param collectionClass 集合类型,例如 ArrayList.class
     * @param clazz           对象类型 例如 User.class
     * @return
     */
    private static Object readValueList(String content, Class collectionClass, Class clazz) {
        if (content == null) {
            return null;
        }

        Object o = null;

        try {
            o = mapper.readValue(content, getCollectionType(collectionClass, clazz));
        } catch (JsonParseException e) {
            logger.error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return o;
    }

    private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }
}
