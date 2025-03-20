package com.essence.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhy
 * @since 2023/3/21 10:20
 */
public class FieldsUtils {
    /**
     * 判断对象中属性值是否全为空
     *
     * @param object
     * @return
     */
    public static boolean checkObjAllFieldsIsNull(Object object) {
        if (null == object) {
            return true;
        }
        Map<String,Object> map = JSONObject.parseObject(JSONObject.toJSONString(object));
        List<Object> collect = map.values().stream().filter(p -> p != null && !"".equals(p)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(collect)){
            return false;
        }
        return true;
    }
}
