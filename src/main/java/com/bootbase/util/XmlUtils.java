package com.bootbase.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.mapper.MapperWrapper;

/**
 * 输出xml和解析xml的工具类
 */
public class XmlUtils {
    /**
     * java 转换成xml
     *
     * @param obj 对象实例
     * @return String xml字符串
     */
    public static String toXml(Object obj) {
        XStream xstream = new XStream();
        // 如果没有这句，xml中的根元素会是<包.类名>；或者说：注解根本就没生效，所以的元素名就是类的属性
        // 通过注解方式的，一定要有这句话
        xstream.processAnnotations(obj.getClass());
        return xstream.toXML(obj);
    }

    /**
     * 将传入xml文本转换成Java对象
     *
     * @param xmlStr
     * @param cls    xml对应的class类
     * @return T   xml对应的class类的实例对象
     */
    public static <T> T toBean(String xmlStr, Class<T> cls) {
        // 注意：不是new Xstream(); 否则报错：java.lang.NoClassDefFoundError: org/xmlpull/v1/XmlPullParserFactory
        XStream xstream = new XStream(new DomDriver()) {
            @Override
            protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new MapperWrapper(next) {
                    @Override
                    public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                        if (definedIn == Object.class) {
                            return false;
                        }
                        return super.shouldSerializeMember(definedIn, fieldName);
                    }
                };
            }
        };
        xstream.processAnnotations(cls);
        return (T) xstream.fromXML(xmlStr);
    }

    public static <T> T toBean(Object[] objs, Class<T> cls) {
        // 注意：不是new Xstream(); 否则报错：java.lang.NoClassDefFoundError: org/xmlpull/v1/XmlPullParserFactory
        XStream xstream = new XStream(new DomDriver()) {
            @Override
            protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new MapperWrapper(next) {
                    @Override
                    public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                        if (definedIn == Object.class) {
                            return false;
                        }
                        return super.shouldSerializeMember(definedIn, fieldName);
                    }
                };
            }
        };
        xstream.processAnnotations(cls);
        return (T) xstream.fromXML(String.valueOf(objs[0]));
    }
}
