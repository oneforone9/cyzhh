package com.essence.common.utils;


import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

/**
 * @author zhy
 * @since 2022/9/20 16:22
 */
public class GisUtils {
    /**
     * 两点距离 WGS84坐标系
     *
     * @param longitude1 x经度
     * @param latitude1 x纬度
     * @param longitude2 y经度
     * @param latitude2 y纬度
     * @return 点x到点y距离(m)
     */
    public static Double distance(Double longitude1,Double latitude1,Double longitude2,Double latitude2){
        GlobalCoordinates firstPoint = new GlobalCoordinates(latitude1, longitude1);
        GlobalCoordinates secondPoint = new GlobalCoordinates(latitude2, longitude2);
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.WGS84, firstPoint, secondPoint);
        return geoCurve.getEllipsoidalDistance();
    }

    /**
     * 判断点到图形的距离是否小于距离参数
     * @param pointWkt 格式WKT eg: POINT (8.671532846715365 59.255474452554836)
     * @param polygonWkt 格式WKT eg: MULTIPOLYGON(((8.671532846715365 59.255474452554836,113.67123343062497 64.67799420101966,119.47445255474474 -47.69343065693431,14.474751970835143 -53.11595040539913,8.671532846715365 59.255474452554836)))
     * @param distance 距离单位米
     * @return
     */
    public static boolean intersects(String pointWkt, String polygonWkt, double distance) throws ParseException {
        GeometryFactory geometryFactory = new GeometryFactory();
        System.out.println(geometryFactory.getSRID());
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

