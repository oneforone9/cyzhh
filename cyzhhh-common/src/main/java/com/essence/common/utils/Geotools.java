package com.essence.common.utils;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

/**
 * @author zhy
 * @since 2022/11/10 14:39
 */
public class Geotools {
    /**
     * 判断点到图形的距离是否小于距离参数
     * @param pointWkt 格式WKT eg: POINT (8.671532846715365 59.255474452554836)
     * @param polygonWkt 格式WKT eg: MULTIPOLYGON(((8.671532846715365 59.255474452554836,113.67123343062497 64.67799420101966,119.47445255474474 -47.69343065693431,14.474751970835143 -53.11595040539913,8.671532846715365 59.255474452554836)))
     * @param distance 距离单位米
     * @return
     */
    public static boolean intersects(String pointWkt, String polygonWkt, double distance) throws ParseException {
        GeometryFactory geometryFactory = new GeometryFactory();
        WKTReader reader = new WKTReader(geometryFactory);
        // 点
        Geometry point = reader.read(pointWkt);
        // 面
        Geometry polygon = reader.read(polygonWkt);
        // 点buffer一个范围
        Geometry buffer = point.buffer(distance / (2 * Math.PI * 6371004) * 360);
        // buffer范围与面是否相交
        return polygon.intersects(buffer);
    }

}
