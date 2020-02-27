package com.walle.util;

import org.apache.commons.collections4.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtil {
    /**
     * String is null or empty
     */
    public static boolean isEmpty(String str) {
        return isEmpty(str, true);
    }

    public static boolean isEmpty(String str, boolean trim) {
        return str == null || str.isEmpty() || (trim && str.trim().isEmpty());
    }

    public static boolean chkLen(String str, int minLen, int maxLen) {
        return str != null
                && str.length() >= Math.max(minLen, 0)
                && str.length() <= Math.min(Math.max(maxLen, 0), 1024);
    }

    public static String mask(String str) {
        return mask(str, 1, 12);
    }

    public static String mask(String str, int minLen, int maxLen) {
        if (isEmpty(str)) {
            return "";
        }

        str = str.trim().replace(" ", "")
                .replace("-", "")
                .replace("_", "");
        if (str.length() <= minLen && minLen > 0) {
            return str;
        }

        // Remove the middle part if it's too long
        if (maxLen > 0) {
            if (maxLen % 2 != 0) {
                maxLen++;
            }

            if (str.length() > maxLen) {
                str = String.format("%s%s",
                        str.substring(0, maxLen / 2),
                        str.substring(str.length() - maxLen / 2, str.length())
                );
            }
        }

        // Divide 3 parts: str+mask+str
        final int len = str.length();
        final int maskLen = len > 3 ? len / 3 : 1;
        StringBuilder sb = new StringBuilder(maskLen);
        for (int i = 0; i < maskLen; i++) {
            sb.append("*");
        }

        int startLen = (len - maskLen) / 2;
        return String.format("%s%s%s",
                str.substring(0, startLen), sb.toString(), str.substring(startLen + maskLen, len)
        );
    }

    public static boolean matches(String str, String pattern) {
        if (isEmpty(str) || isEmpty(pattern)) {
            return false;
        }
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static String[] parse(String str, String pattern) {
        if (isEmpty(str) || isEmpty(pattern)) {
            return null;
        }

        List<String> list = new ArrayList<String>();
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        while (m.find()) {
            list.add(m.group());
        }
        if (list.size() <= 0) {
            return null;
        }

        String[] arr = new String[list.size()];
        list.toArray(arr);
        return arr;
    }

    public static boolean contains(String str, String subStr) {
        return contains(str, subStr, ",");
    }

    public static boolean contains(String str, String subStr, String separator) {
        if (isEmpty(str) || isEmpty(subStr)) {
            return false;
        }

        if (subStr.equalsIgnoreCase(str)) {
            return true;
        }

        String[] strArray = split(str, separator);
        if (strArray == null) {
            return false;
        }

        for (String tmpStr : strArray) {
            if (tmpStr.trim().length() == 0 && subStr.trim().length() == 0) {
                return true;
            }

            if (tmpStr.trim().equalsIgnoreCase(subStr.trim())) {
                return true;
            }
        }
        return false;
    }

    public static String[] split(String str, String separator) {
        if (isEmpty(str) || isEmpty(separator)) {
            return null;
        }
        return str.split(separator);
    }

    public static String joinObj(Object[] objArr, String separator) {
        return objArr == null ? null : joinObj(Arrays.asList(objArr), separator);
    }

    public static String joinObj(Collection<Object> objList, String separator) {
        if (CollectionUtils.isEmpty(objList)) {
            return null;
        }

        List<String> strList = new ArrayList<String>();
        for (Object obj : objList) {
            strList.add(obj == null ? "" : obj.toString());
        }
        return join(strList, separator);
    }

    public static String join(String[] strArr, String separator) {
        return strArr == null ? null : join(Arrays.asList(strArr), separator);
    }

    public static String join(Collection<String> strList, String separator) {
        if (CollectionUtils.isEmpty(strList) || separator == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (String str : strList) {
            sb.append(separator);
            sb.append(str);
        }
        return sb.substring(separator.length());
    }

    /**
     * Get the bytes with UTF-8
     */
    public static byte[] getBytes(String str) {
        if (str == null) {
            return null;
        }
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String trimChinese(String str) {
        return isEmpty(str) ? "" : str.replaceAll("[\u4e00-\u9fa5]+", "");
    }
}
