package com.xinput.bootbase.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @Author: xinput
 */
public class StreamUtils {

    /**
     * List<T> ==> List<Field>
     * ==> students.stream().map(Student::getName).collect(Collectors.toList());
     *
     * @param collection List<User>
     * @param function   User::getName
     * @return
     */
    public static <R, A> List<A> collectColumn(Collection<R> collection, Function<R, A> function) {
        return collection.stream()
                .map(function)
                .collect(Collectors.toList());
    }

    /**
     * List<T> ==> List<Field>
     *
     * @param collection List<User>
     * @param predicate  user -> user.getAge()>10,
     * @param function   User::getName
     * @return
     */
    public static <R, A> List<A> collectColumn(Collection<R> collection, Predicate<R> predicate, Function<R, A> function) {
        return collection.stream()
                .filter(predicate)
                .map(function)
                .collect(Collectors.toList());
    }

    /**
     * List<T> ==> List<Field> 去重
     * ==> students.stream().map(Student::getAge).distinct().collect(Collectors.toList())
     *
     * @param collection List<User>
     * @param function   User::getName
     * @return
     */
    public static <R, A> List<A> collectDistinctColumn(Collection<R> collection, Function<R, A> function) {
        return collection.stream()
                .map(function)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * List<T> ==> List<Field> 去重
     *
     * @param collection List<User>
     * @param predicate  -> user.getAge()>10,
     * @param function   User::getName
     * @return
     */
    public static <R, A> List<A> collectDistinctColumn(Collection<R> collection, Predicate<R> predicate, Function<R, A> function) {
        return collection.stream()
                .filter(predicate)
                .map(function)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 分组 List<T> => Map<String,List<T>>
     * ==> students.stream().collect(Collectors.groupingBy(Student::getAge));
     *
     * @param collection List<User>
     * @param function   User::getId
     */
    public static <R, A> Map<A, List<R>> group(Collection<R> collection, Function<R, A> function) {
        return collection.stream()
                .collect(Collectors.groupingBy(function));
    }

    /**
     * 分组 List<T> => Map<String,T>
     * ==> students.stream().collect(Collectors.toMap(Student::getAge, k1 -> k1, (k1, k2) -> k2));
     *
     * @param collection    List<User>
     * @param function      User::getId
     * @param valueMapper   id -> id
     * @param mergeFunction (k1, k2) -> k1
     * @param <R>
     * @param <A>
     * @param <P>
     * @return
     */
    public static <R, A, P> Map<A, P> group(Collection<R> collection, Function<R, A> function,
                                            Function<R, P> valueMapper,
                                            BinaryOperator<P> mergeFunction) {
        return collection.stream()
                .collect(Collectors.toMap(function, valueMapper, mergeFunction));
    }

    /**
     * 集合转Map
     * List<T> => Map<String,Object>
     * ==> students.stream().collect(Collectors.toMap(Student::getName, Student::getAge, (k1, k2) -> k2));
     *
     * @param collection    集合List<User>
     * @param keyMapper     字段User::getId
     * @param valMapper     字段User::getName
     * @param mergeFunction (k2, k1) -> k2
     * @return
     */
    public static <R, K, V> Map<K, V> tomap(Collection<R> collection, Function<R, K> keyMapper,
                                            Function<R, V> valMapper, BinaryOperator<V> mergeFunction) {
        return collection.stream().collect(Collectors.toMap(keyMapper, valMapper, mergeFunction));
    }

    /**
     * 拼接字符串
     */
    public static String union(List<String> list) {
        return list.stream()
                .collect(Collectors.joining(","));
    }

    /**
     * 使用指定连接符将集合中的参数拼接在一起
     *
     * @param list      指定集合数据
     * @param delimiter 拼接符
     * @return
     */
    public static String union(List<String> list, String delimiter) {
        return list.stream()
                .collect(Collectors.joining(delimiter));
    }

    /**
     * 使用指定连接符将集合中的参数拼接在一起
     *
     * @param collection 指定集合数据
     * @param delimiter  拼接符
     * @param prefix     前缀符号
     * @param suffix     后缀符号
     * @return
     */
    public static String union(Collection<String> collection, String delimiter, String prefix, String suffix) {
        return collection.stream()
                .collect(Collectors.joining(delimiter, prefix, suffix));
    }

    /**
     * 将集合中的所有非空字符串拼接
     *
     * @param collection
     * @param delimiter
     * @return
     */
    public static String unionNotEmptyString(Collection<String> collection, String delimiter) {
        return collection.stream()
                .filter(col -> StringUtils.isNotBlank(col))
                .collect(Collectors.joining(delimiter));
    }

    public static String unionNotEmptyString(List<String> collection, String delimiter, String prefix, String suffix) {
        return collection.stream()
                .filter(col -> StringUtils.isNotBlank(col))
                .collect(Collectors.joining(delimiter, prefix, suffix));
    }

    /**
     * 根据条件筛选filter
     */
    public static <R> List<R> filter(Collection<R> collection, Predicate<R> predicate) {
        return collection.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
}
