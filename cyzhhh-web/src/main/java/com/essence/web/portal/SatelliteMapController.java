package com.essence.web.portal;

import com.essence.common.common.SystemSecurityMessage;
import com.essence.common.dto.QixiangImageDto;
import com.essence.common.utils.ObtainQixiangImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 卫星图雷达图
 * @Author: liwy
 * @CreateTime: 2022-11-07  16:39
 */
@RestController
@RequestMapping("SatelliteMap")
public class SatelliteMapController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    //获取卫星云图
    @Value("${nmc.wx.path}")
    private String nmcWxPath;
    //获取雷达回波图华北地区的
    @Value("${nmc.ld.path}")
    private String nmcLdPath;
    //获取雷达回波图单站大兴的
    @Value("${nmc.lddz.path}")
    private String nmcLddzPath;

    /**
     * 获取雷达回波图  --华北地区的
     * @return
     */
    @RequestMapping(value = "/obtainRadarMap", method = RequestMethod.GET)
    public SystemSecurityMessage obtainRadarMap() {
        try {
            ObtainQixiangImage obtainQixiangImage = new ObtainQixiangImage();
            String  path = nmcLdPath;
            List<QixiangImageDto> qixiangImageDtos = obtainQixiangImage.ObtainRadarMap(path);
            return new SystemSecurityMessage("ok", "获取雷达回波图成功！", qixiangImageDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return new SystemSecurityMessage("error", "获取雷达回波图失败!");
        }
    }

    /**
     * 获取雷达回波图  --单站雷达-大兴的
     * @return
     */
    @RequestMapping(value = "/obtainRadarMapDz", method = RequestMethod.GET)
    public SystemSecurityMessage obtainRadarMapDz() {
        try {
            ObtainQixiangImage obtainQixiangImage = new ObtainQixiangImage();
            String  path = nmcLddzPath;
            List<QixiangImageDto> qixiangImageDtos = obtainQixiangImage.ObtainRadarMap(path);
            return new SystemSecurityMessage("ok", "获取雷达回波图成功-单站！", qixiangImageDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return new SystemSecurityMessage("error", "获取雷达回波图失败-单站!");
        }
    }

    /**
     * 获取卫星云图
     * @return
     */
    @RequestMapping(value = "/obtainSatellitCloudImage", method = RequestMethod.GET)
    public SystemSecurityMessage obtainSatellitCloudImage() {
        try {
            ObtainQixiangImage obtainQixiangImage = new ObtainQixiangImage();
            String  path = nmcWxPath;
            List<QixiangImageDto> qixiangImageDtos = obtainQixiangImage.ObtainSatellitCloudImage(path);
            return new SystemSecurityMessage("ok", "获取卫星云图成功！", qixiangImageDtos);
        } catch (Exception e) {
            logger.error("获取卫星云图失败", e);
            return new SystemSecurityMessage("error", "获取卫星云图失败!");
        }
    }




}
