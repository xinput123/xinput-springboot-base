package com.xinput.bootbase.util;

import com.xinput.bootbase.config.BootBaseConfig;

public class PageUtils {

    private static final int MAX_OFFSET = BootBaseConfig.getMaxOffset();

    private static final int MAX_LIMIT = BootBaseConfig.getMaxLimit();

    private static final int DEFAULT_LIMIT = BootBaseConfig.getDefaultLimit();

    /**
     * 验证limit参数，参数可以为null
     */
    public static Integer validateLimit(Integer limit) {
        if (limit == null) {
            return null;
        }

        if (limit < 0) {
            return DEFAULT_LIMIT;
        }

        return limit > MAX_LIMIT ? MAX_LIMIT : limit;
    }

    /**
     * 验证offset参数,参数可以为null
     */
    public static Integer validateOffset(Integer offset) {
        if (offset == null) {
            return null;
        }
        if (offset < 0) {
            return 0;
        }
        return offset > MAX_OFFSET ? MAX_OFFSET : offset;
    }

    /**
     * 验证limit参数，参数不能为空
     */
    public static int safeLimit(Integer limit) {
        if (limit == null || limit < 0) {
            return DEFAULT_LIMIT;
        }
        return limit > MAX_LIMIT ? MAX_LIMIT : limit;
    }

    /**
     * 验证offset参数，参数不能为空
     */
    public static int safeOffset(Integer offset) {
        if (offset == null || offset < 0) {
            return 0;
        }
        return offset > MAX_OFFSET ? MAX_OFFSET : offset;
    }
}
