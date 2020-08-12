package com.xinput.bootbase.util;

/**
 * 数据脱敏
 *
 * @Author: xinput
 * @Date: 2020-06-11 13:09
 */
public class DesensitizeUtils {

    /**
     * 手机号格式校验正则,目前手机号的号段开启太多，所有直接用最简单方式
     */
    public static final String PHONE_REGEX = "^1\\d{10}$";

    /**
     * 手机号脱敏筛选正则
     */
    public static final String PHONE_BLUR_REGEX = "(\\d{3})\\d{4}(\\d{4})";

    /**
     * 手机号脱敏替换正则
     */
    public static final String PHONE_BLUR_REPLACE_REGEX = "$1****$2";

    /**
     * 【中文姓名】只显示第一个汉字，其他隐藏为2个星号，比如：李**
     *
     * @param fullName
     * @return
     */
    public static String chineseName(String fullName) {
        if (StringUtils.isBlank(fullName)) {
            return "";
        }
        String name = StringUtils.left(fullName, 1);
        return StringUtils.rightPad(name, StringUtils.length(fullName), "*");
    }

    /**
     * 手机号格式校验
     *
     * @param phone
     * @return
     */
    public static final boolean checkPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        return phone.matches(PHONE_REGEX);
    }

    /**
     * 身份证号格式校验: 这里只简单的校验长度
     *
     * @param idCard
     * @return
     */
    public static final boolean checkIdCard(String idCard) {
        if (StringUtils.isEmpty(idCard)) {
            return false;
        }

        int idCardLength = idCard.length();
        return StringUtils.IDCARD_15_LENGTH == idCardLength || StringUtils.IDCARD_18_LENGTH == idCardLength;
    }

    /**
     * 脱敏手机号码
     *
     * @param phone
     * @return
     */
    public static final String phone(String phone) {
        boolean checkFlag = checkPhone(phone);
        if (!checkFlag) {
            return phone;
        }
        return phone.replaceAll(PHONE_BLUR_REGEX, PHONE_BLUR_REPLACE_REGEX);
    }

    /**
     * 脱敏身份证号码
     * 15位：前六位，后三位
     * 18位：前六位，后四位
     *
     * @param idCard
     * @return
     */
    public static final String idCard(String idCard) {
        if (StringUtils.isBlank(idCard)) {
            return StringUtils.EMPTY;
        }

        String newIdCard = StringUtils.EMPTY;
        int length = idCard.length();
        switch (length) {
            case StringUtils.IDCARD_15_LENGTH:
                newIdCard = StringUtils.left(idCard, 6).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(idCard, 3), StringUtils.length(idCard), "*"), "******"));
                break;
            case StringUtils.IDCARD_18_LENGTH:
                newIdCard = StringUtils.left(idCard, 6).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(idCard, 4), StringUtils.length(idCard), "*"), "******"));
                break;
            default:
                newIdCard = idCard;
                break;
        }
        return newIdCard;
    }

    /**
     * 【银行卡号】前六位，后四位，其他用星号隐藏每位1个星号，比如：6222600**********1234
     *
     * @param cardNum
     * @return
     */
    public static String bankCard(String cardNum) {
        if (StringUtils.isBlank(cardNum)) {
            return "";
        }
        return StringUtils.left(cardNum, 6).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(cardNum, 4), StringUtils.length(cardNum), "*"), "******"));
    }

    /**
     * 【密码】密码的全部字符都用六个*代替，******
     *
     * @param password
     * @return
     */
    public static String password(String password) {
        return StringUtils.EXAMPLE_PASSWORD;
    }
}
