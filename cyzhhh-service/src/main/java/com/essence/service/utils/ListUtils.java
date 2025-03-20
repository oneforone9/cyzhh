package com.essence.service.utils;

import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author BINX
 * @Description TODO
 * @Date 2023/5/26 15:13
 */
public class ListUtils {

    /**
     * 根据实体的字段合并两个list的实体数据
     * @param list1
     * @param list2
     * @param clazz 实体
     * @param sameValueDeclared list1 和 list2 相同的实体字段
     * @return
     * @param <T>
     */
    public static <T> List<T> mergeListBySameDeclared(List<T> list1, List<T> list2, Class clazz, String... sameValueDeclared) {
        if(CollectionUtils.isEmpty(list1)) {
            return list2;
        }
        if(CollectionUtils.isEmpty(list2)) {
            return list1;
        }
        if (!clazz.equals(list1.get(0).getClass()) || !clazz.equals(list2.get(0).getClass())) {
            throw new IllegalArgumentException("The declared element type of the lists are different");
        }

        // Create a map of the declared fields' values to objects from list1.
        Map<List<Object>, T> map = new HashMap<>();
        for (T t : list1) {
            List<Object> values = null;
            try {
                values = getValues(t, sameValueDeclared);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            map.put(values, t);
        }

        // Merge objects from list2 into the map, overwriting any previous values with the same field values.
        for (T t : list2) {
            List<Object> values = null;
            try {
                values = getValues(t, sameValueDeclared);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (map.containsKey(values)) {
                T original = map.get(values);
                try {
                    mergeObjects(original, t);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } else {
                map.put(values, t);
            }
        }

        // Return the merged objects as a list.
        return new ArrayList<>(map.values());
    }

    private static <T> List<Object> getValues(T object, String... fieldNames) throws IllegalAccessException {
        List<Object> values = new ArrayList<>();
        for (String fieldName : fieldNames) {
            Field field;
            try {
                field = object.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(object);
                values.add(value);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                return Collections.emptyList();
            }
        }
        return values;
    }

    private static <T> void mergeObjects(T target, T source) throws IllegalAccessException {
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(source);
            if (value != null) {
                field.set(target, value);
            }
        }
    }
}
