package com.xinput.bootbase.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtils {
    private static final Pattern P = Pattern.compile("(([0-9]+?)((d|h|mi|min|mn|s)))+?");
    private static final Integer MINUTE = 60;
    private static final Integer HOUR = 60 * MINUTE;
    private static final Integer DAY = 24 * HOUR;

    /**
     * Parse a duration
     *
     * @param duration 3h, 2mn, 7s or combination 2d4h10s, 1w2d3h10s
     * @return The number of seconds
     */
    public static int parseDuration(String duration) {
        if (duration == null) {
            return 30 * DAY;
        }

        Matcher matcher = P.matcher(duration);
        int seconds = 0;
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid duration pattern : " + duration);
        }

        matcher.reset();
        while (matcher.find()) {
            if ("d".equals(matcher.group(3))) {
                seconds += Integer.parseInt(matcher.group(2)) * DAY;
            } else if ("h".equals(matcher.group(3))) {
                seconds += Integer.parseInt(matcher.group(2)) * HOUR;
            } else if ("mi".equals(matcher.group(3)) || "min".equals(matcher.group(3)) || "mn".equals(matcher.group(3))) {
                seconds += Integer.parseInt(matcher.group(2)) * MINUTE;
            } else {
                seconds += Integer.parseInt(matcher.group(2));
            }
        }

        return seconds;
    }
}
