package com.xinput.bootbase.util;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @Author: xinput
 * @Date: 2020-06-18 19:12
 */
public class BeanUtils {

    private static final Logger logger = Logs.get();

    private static final Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    /**
     * List  实体类 转换器
     *
     * @param source 原数据
     * @param clz    转换类型
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T, S> List<T> convertor(List<S> source, Class<T> clz) {
        if (CollectionUtils.isEmpty(source)) {
            return Lists.newArrayList();
        }

        List<T> list = Lists.newArrayListWithCapacity(source.size());
        source.forEach(s -> list.add(mapper.map(s, clz)));

        return list;
    }

    /**
     * Set 实体类 深度转换器
     *
     * @param source 原数据
     * @param clz    目标对象
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T, S> Set<T> convertor(Set<S> source, Class<T> clz) {
        if (source == null) {
            return null;
        }
        Set<T> set = new TreeSet<>();
        for (S s : source) {
            set.add(mapper.map(s, clz));
        }
        return set;
    }

    /**
     * 实体类 深度转换器
     *
     * @param source
     * @param clz
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T, S> T convertor(S source, Class<T> clz) {
        if (source == null) {
            return null;
        }
        return mapper.map(source, clz);
    }

    public static void convertor(Object source, Object object) {
        mapper.map(source, object);
    }

    public static <T> void copyConvertor(T source, Object object) {
        mapper.map(source, object);
    }

}
