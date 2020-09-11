package com.xinput.baseboot.util;

import org.junit.Test;
import org.springframework.http.HttpMethod;

/**
 * @author xinput
 * @date 2020-08-12 18:08
 */
public class StreamUtilDemo {

    @Test
    public void test() {
        System.out.println(HttpMethod.OPTIONS.toString());
    }

    /**
     * 产生一个新流，其中按自然顺序排序
     */
//    @Test
//    void testSortedComparator() {
//        empList.stream().sorted((x, y) -> {
//            if (x.getAge() == y.getAge()) {
//                return x.getName().compareTo(y.getName());
//            } else {
//                return Integer.compare(x.getAge(), y.getAge());
//            }
//        }).forEach(System.out::println);
//    }
}
