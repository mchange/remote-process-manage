package com.rpm.utils;

public class StringUtils {

    public static String join(String[] items, int begin, String split) {
        return join(items, begin, -1, split);
    }

    /**
     * 将数组的指定范围的元素合并成字符串
     *
     * @param items 数组
     * @param begin 开始下标（闭：包含）
     * @param end   结束下标（开：不包含）
     * @param split 连接分隔符
     * @return
     */
    public static String join(String[] items, int begin, int end, String split) {
        if (null == items) {
            return "";
        }
        if (begin < end) {
            return "";

        }
        int len = items.length;
        if (len < begin || len < end) {
            return "";
        }

        end = end < 0 ? len : end;

        StringBuffer sb = new StringBuffer();
        for (int i = begin; i < end; i++) {
            sb.append(items[i]).append(split);
        }
        return sb.substring(0, sb.lastIndexOf(split));

    }
}
