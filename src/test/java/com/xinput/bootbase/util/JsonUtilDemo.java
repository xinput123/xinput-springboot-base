package com.xinput.bootbase.util;

import com.xinput.bleach.util.BuilderUtils;
import com.xinput.bleach.util.JsonUtils;
import com.xinput.bootbase.domain.Student;
import org.junit.Test;

/**
 * @author xinput
 * @date 2020-08-06 14:06
 */
public class JsonUtilDemo {

    @Test
    public void test() {
        Student s0 = null;
        System.out.println(JsonUtils.toJsonString(s0));
        System.out.println(JsonUtils.toJsonString(s0, true));

        Student s1 = create(1);
        System.out.println(s1.toString());
        System.out.println(JsonUtils.toJsonString(s1));
        System.out.println(JsonUtils.toJsonString(s1, true));

        String s = null;
        Student s2 = JsonUtils.toBean(s, Student.class);
        System.out.println(s2 == null);

        Student s3 = JsonUtils.toBean("", Student.class);

    }

    private Student create(int id) {
        return BuilderUtils.of(Student::new)
                .with(Student::setId, String.valueOf(id))
                .with(Student::setName, "学生" + id)
                .with(Student::setAge, 10 + id)
                .build();
    }
}
