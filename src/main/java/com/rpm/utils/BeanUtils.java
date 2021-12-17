package com.rpm.utils;

import java.lang.reflect.Field;

public class BeanUtils {

    /**
     * 将实例为空的参数设置为null
     * @param <T>
     * @param t
     * @return
     */
    public static <T> void setEmptyToNull(T t){
        Class clazz = t.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if ("".equals(String.valueOf(field.get(t)).trim())) {
                    field.set(t, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
