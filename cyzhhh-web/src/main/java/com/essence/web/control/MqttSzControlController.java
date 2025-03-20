package com.essence.web.control;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.utils.HttpUtils;
import com.essence.common.utils.ResponseResult;
import com.essence.dao.StGaConvertDao;
import com.essence.dao.StSideGateDao;
import com.essence.dao.WaterGateControlDeviceDao;
import com.essence.dao.entity.StGaConvertDto;
import com.essence.dao.entity.StSideGateDto;
import com.essence.dao.entity.WaterGateControlDevice;
import com.essence.interfaces.dot.PublishDTO;
import com.essence.interfaces.dot.WaterGatePushDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * mqtt 水闸远程调用 控制操作
 * M0.1  1 开启
 * M0.0  1 停止
 * M0.2  1 关闭
 */
@RestController
@RequestMapping("control")
public class MqttSzControlController {
    @Resource
    private StGaConvertDao stGaConvertDao;
    @Resource
    private WaterGateControlDeviceDao waterGateControlDeviceDao;
    @Resource
    private StSideGateDao stSideGateDao;

    //获取 mqtt 服务的水闸控制开关 在 iot 服务内提供 http 请求调用
    @Value("${control.sz.url}")
    private String controlSzUrl;
    
    /**
     * 水闸控制开关
     *
     * @param model  几个孔
     * @param stcd   站点 id
     * @param num    第几个闸孔
     * @param status 1开 2停 3关
     * @return
     */
    @GetMapping("sz/status")
    public ResponseResult controlSZStatus(@RequestParam(required = false) Integer model, String stcd, Integer num, Integer status) {
        if (model == null) {
            model = 1;
        }
        String lot = "";
        //需要查看水闸是 几标段的，因为不同的标段 所对应的控制符号不一样
        QueryWrapper gateQuery = new QueryWrapper();
        gateQuery.eq("stcd", stcd);
        List<StSideGateDto> stSideGateDtos = stSideGateDao.selectList(gateQuery);
        if (CollUtil.isNotEmpty(stSideGateDtos)) {
            if (stSideGateDtos.size() > 1) {
                return ResponseResult.error("水闸站点 id 有重复请排查", null);
            }
            StSideGateDto stSideGateDto = stSideGateDtos.get(0);
            lot = stSideGateDto.getLot();
        }
        List<StGaConvertDto> stGaConvertDtos = stGaConvertDao.selectList(new QueryWrapper<>());
        Map<String, String> szRelationMap = stGaConvertDtos.parallelStream().collect(Collectors.toMap(StGaConvertDto::getStcd, StGaConvertDto::getSn, (o1, o2) -> o2));
        String s = szRelationMap.get(stcd);
        if (StrUtil.isEmpty(s)) {
            return ResponseResult.error("水闸远控没有对应关系 请添加对应关系", null);
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("did", s);
        WaterGateControlDevice waterGateControlDevice = waterGateControlDeviceDao.selectOne(queryWrapper);
        if (waterGateControlDevice == null) {
            return ResponseResult.error("硬件没有回传数据 或者 plc 硬件没有pid关系 请添加对应关系", null);
        }
        if (StrUtil.isEmpty(lot)){
            return ResponseResult.error("", "相关水闸没有 标志标段 lot 缺失");
        }
        switch (lot) {
            case "1":
                //标段 1
                deal1Lot(num,  status,  s, waterGateControlDevice);
                break;
            case "2":
                //标段 2
                break;
            case "3":
                //标段 3
                break;
            case "4":
                deal4Lot(num,  status,  s, waterGateControlDevice);
                //标段 4
                break;
            case "5":
                //标段 5
                //控制下发的指令
                WaterGatePushDTO waterGatePushDTO = new WaterGatePushDTO();
                waterGatePushDTO.setDid(s);
                waterGatePushDTO.setUtime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                List<PublishDTO> downCommend = getDownCommend(num, status, waterGateControlDevice.getPid());
                waterGatePushDTO.setContent(downCommend);

                WaterGatePushDTO reWaterGatePushDTO = new WaterGatePushDTO();
                reWaterGatePushDTO.setDid(s);
                reWaterGatePushDTO.setUtime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                List<PublishDTO> revertDownCommend = getRevertDownCommend(num, status, waterGateControlDevice.getPid());
                reWaterGatePushDTO.setContent(revertDownCommend);
                try {
                    HttpUtils.doPost(controlSzUrl, JSONObject.toJSONString(waterGatePushDTO));
                    //复位指令
                    HttpUtils.doPost(controlSzUrl, JSONObject.toJSONString(reWaterGatePushDTO));
                } catch (Exception e) {
                    return ResponseResult.error("远程请求调用失败  网络异常", null);
                }
                break;
        }

        return ResponseResult.success("发送成功", null);
    }

    /**
     * fix 2023-12-06 单独处理一个一标段的翻板闸
     * 处理 1 标段
     * @param num
     * @param status
     * @param sn
     * @param waterGateControlDevice
     */
    public ResponseResult deal1Lot(int num, Integer status, String sn, WaterGateControlDevice waterGateControlDevice){

        if (sn.equals("FG6080220982")){
            //控制下发的指令
            WaterGatePushDTO waterGatePushDTO = new WaterGatePushDTO();
            waterGatePushDTO.setDid(sn);
            waterGatePushDTO.setUtime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            List<PublishDTO> downCommend = getDownCommend1Sp(num, status, waterGateControlDevice.getPid());
            waterGatePushDTO.setContent(downCommend);

            WaterGatePushDTO reWaterGatePushDTO = new WaterGatePushDTO();
            reWaterGatePushDTO.setDid(sn);
            reWaterGatePushDTO.setUtime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            List<PublishDTO> revertDownCommend = getRevertDownCommend1Sp(num, status, waterGateControlDevice.getPid());
            reWaterGatePushDTO.setContent(revertDownCommend);
            try {
                HttpUtils.doPost(controlSzUrl, JSONObject.toJSONString(waterGatePushDTO));
                //复位指令
                HttpUtils.doPost(controlSzUrl, JSONObject.toJSONString(reWaterGatePushDTO));
            } catch (Exception e) {
                return ResponseResult.error("远程请求调用失败  网络异常", null);
            }
        }else {
            //控制下发的指令
            WaterGatePushDTO waterGatePushDTO = new WaterGatePushDTO();
            waterGatePushDTO.setDid(sn);
            waterGatePushDTO.setUtime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            List<PublishDTO> downCommend = getDownCommend1(num, status, waterGateControlDevice.getPid());
            waterGatePushDTO.setContent(downCommend);

            WaterGatePushDTO reWaterGatePushDTO = new WaterGatePushDTO();
            reWaterGatePushDTO.setDid(sn);
            reWaterGatePushDTO.setUtime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            List<PublishDTO> revertDownCommend = getRevertDownCommend1(num, status, waterGateControlDevice.getPid());
            reWaterGatePushDTO.setContent(revertDownCommend);
            try {
                HttpUtils.doPost(controlSzUrl, JSONObject.toJSONString(waterGatePushDTO));
                //复位指令
                HttpUtils.doPost(controlSzUrl, JSONObject.toJSONString(reWaterGatePushDTO));
            } catch (Exception e) {
                return ResponseResult.error("远程请求调用失败  网络异常", null);
            }
        }


        return ResponseResult.success("发送成功", null);
    }

    /**
     * 在给
     * 处理标段 4
     *
     * @param num    水闸 1（V1090） 2（V1190） 3（V1290） 4（V1390） 5（V4090）  .2 停止  .1 下降 .0 上升  addr 传递括号中数字 addrv 传递 .2 .1 .0
     * @param status 1开 2停 3关
     */
    public ResponseResult deal4Lot(int num, Integer status, String sn, WaterGateControlDevice waterGateControlDevice) {
        //控制下发的指令
        WaterGatePushDTO waterGatePushDTO = new WaterGatePushDTO();
        waterGatePushDTO.setDid(sn);
        waterGatePushDTO.setUtime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        List<PublishDTO> downCommend = getDownCommend4(num, status, waterGateControlDevice.getPid());
        waterGatePushDTO.setContent(downCommend);

        List<WaterGatePushDTO> initSend = new ArrayList<>();
        //一些初始化命令
        List<PublishDTO> commendFor4 = getInit01CommendFor4(num, status, waterGateControlDevice.getPid());
        for (PublishDTO publishDTO : commendFor4) {
            List<PublishDTO> initList = new ArrayList<>();
            initList.add(publishDTO);
            WaterGatePushDTO reWaterGatePushDTO = new WaterGatePushDTO();
            reWaterGatePushDTO.setDid(sn);
            reWaterGatePushDTO.setUtime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            reWaterGatePushDTO.setContent(initList);
            initSend.add(reWaterGatePushDTO);
        }



        try {
            for (int i = 0; i < 5; i++) {
                for (WaterGatePushDTO gatePushDTO : initSend) {
                    //初始化指令
                    HttpUtils.doPost(controlSzUrl, JSONObject.toJSONString(gatePushDTO));
                }
            }
            for (int i = 0; i < 5; i++) {
                HttpUtils.doPost(controlSzUrl, JSONObject.toJSONString(waterGatePushDTO));
            }

        } catch (Exception e) {
            return ResponseResult.error("远程请求调用失败  网络异常", null);

        }
        return ResponseResult.success("", null);
    }

    /**
     * @param num    第几个闸孔
     * @param status status 1开 2停 3关
     *               M0.0 是关闭  M0.1 停止  M0.2 是开启
     * @return
     */
    public List<PublishDTO> getDownCommend(int num, Integer status, String pid) {
        List<PublishDTO> res = new ArrayList<>();

        if (num == 1) {
            if (status == 1) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M0.2");
                publishDTO.setAddrv("1");

                res.add(publishDTO);
            }
            if (status == 2) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M0.1");
                publishDTO.setAddrv("1");


                res.add(publishDTO);

            }
            if (status == 3) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M0.0");
                publishDTO.setAddrv("1");

                res.add(publishDTO);

            }
        }
        if (num == 2) {
            if (status == 1) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M1.0");
                publishDTO.setAddrv("1");

                res.add(publishDTO);

            }
            if (status == 2) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M0.7");
                publishDTO.setAddrv("1");


                res.add(publishDTO);

            }
            if (status == 3) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M0.6");
                publishDTO.setAddrv("1");

                res.add(publishDTO);

            }
        }

        return res;
    }

    /**
     * 1标段的指令 特殊处理标段
     * @param num    第几个闸孔
     * @param status status 1开 2停 3关
     *               水闸 1 M0.0 是开启  M0.1 停止  M0.2 是关闭
     * @return
     */
    public List<PublishDTO> getDownCommend1Sp(int num, Integer status, String pid) {
        List<PublishDTO> res = new ArrayList<>();

        if (num == 1) {
            if (status == 1) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M0.2");
                publishDTO.setAddrv("1");

                res.add(publishDTO);
            }
            if (status == 2) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M0.1");
                publishDTO.setAddrv("1");


                res.add(publishDTO);

            }
            if (status == 3) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M0.0");
                publishDTO.setAddrv("1");

                res.add(publishDTO);

            }
        }
        if (num == 2) {
            if (status == 1) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M1.0");
                publishDTO.setAddrv("1");
                res.add(publishDTO);
            }
            if (status == 2) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M0.7");
                publishDTO.setAddrv("1");
                res.add(publishDTO);
            }
            if (status == 3) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M0.6");
                publishDTO.setAddrv("1");
                res.add(publishDTO);
            }
        }
        if (num == 3) {
            if (status == 1) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M1.6");
                publishDTO.setAddrv("1");
                res.add(publishDTO);
            }
            if (status == 2) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M1.5");
                publishDTO.setAddrv("1");
                res.add(publishDTO);
            }
            if (status == 3) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M1.4");
                publishDTO.setAddrv("1");
                res.add(publishDTO);
            }
        }

        return res;
    }

    /**
     * 1标段的指令
     * @param num    第几个闸孔
     * @param status status 1开 2停 3关
     *               水闸 1 M0.0 是开启  M0.1 停止  M0.2 是关闭
     * @return
     */
    public List<PublishDTO> getDownCommend1(int num, Integer status, String pid) {
        List<PublishDTO> res = new ArrayList<>();

        if (num == 1) {
            if (status == 1) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M0.0");
                publishDTO.setAddrv("1");

                res.add(publishDTO);
            }
            if (status == 2) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M0.1");
                publishDTO.setAddrv("1");


                res.add(publishDTO);

            }
            if (status == 3) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M0.2");
                publishDTO.setAddrv("1");

                res.add(publishDTO);

            }
        }
        if (num == 2) {
            if (status == 1) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M0.6");
                publishDTO.setAddrv("1");
                res.add(publishDTO);
            }
            if (status == 2) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M0.7");
                publishDTO.setAddrv("1");
                res.add(publishDTO);
            }
            if (status == 3) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M1.0");
                publishDTO.setAddrv("1");
                res.add(publishDTO);
            }
        }
        if (num == 3) {
            if (status == 1) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M1.4");
                publishDTO.setAddrv("1");
                res.add(publishDTO);
            }
            if (status == 2) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M1.5");
                publishDTO.setAddrv("1");
                res.add(publishDTO);
            }
            if (status == 3) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M1.6");
                publishDTO.setAddrv("1");
                res.add(publishDTO);
            }
        }

        return res;
    }

    /**
     * 1（V1090） 2（V1190） 3（V1290） 4（V1390） 5（V4090）   .2 停止  .1 下降 .0 上升  addr 传递括号中数字 addrv 传递 .2 .1 .0
     *
     * @param status 1开 2停 3关
     * @param num
     * @param status
     * @param pid
     * @return
     */
    public List<PublishDTO> getDownCommend4(int num, Integer status, String pid) {
        List<PublishDTO> res = new ArrayList<>();

        if (num == 1) {
            if (status == 1) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("V1090.0");
                publishDTO.setAddrv("1");
                res.add(publishDTO);
            }
            if (status == 2) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("V1090.2");
                publishDTO.setAddrv("1");
                res.add(publishDTO);
            }
            if (status == 3) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("V1090.1");
                publishDTO.setAddrv("1");
                res.add(publishDTO);

            }
        }
        if (num == 2) {
            if (status == 1) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("V1190.0");
                publishDTO.setAddrv("1");
                res.add(publishDTO);

            }
            if (status == 2) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("V1190.2");
                publishDTO.setAddrv("1");

                res.add(publishDTO);

            }
            if (status == 3) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("V1190.1");
                publishDTO.setAddrv("1");

                res.add(publishDTO);
            }
        }
        if (num == 3) {
            if (status == 1) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("V1290.0");
                publishDTO.setAddrv("1");

                res.add(publishDTO);

            }
            if (status == 2) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("V1290.2");
                publishDTO.setAddrv("1");
                res.add(publishDTO);

            }
            if (status == 3) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("V1290.1");
                publishDTO.setAddrv("1");
                res.add(publishDTO);

            }
        }
        if (num == 4) {
            if (status == 1) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("V1390.0");
                publishDTO.setAddrv("1");
                res.add(publishDTO);

            }
            if (status == 2) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("V1390.2");
                publishDTO.setAddrv("1");
                res.add(publishDTO);

            }
            if (status == 3) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("V1390.1");
                publishDTO.setAddrv("1");
                res.add(publishDTO);
            }
        }
        if (num == 5) {
            if (status == 1) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("V1490.0");
                publishDTO.setAddrv("1");
                res.add(publishDTO);
            }
            if (status == 2) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("V1490.2");
                publishDTO.setAddrv("1");
                res.add(publishDTO);
            }
            if (status == 3) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("V1490.1");
                publishDTO.setAddrv("1");
                res.add(publishDTO);
            }
        }
        if (num == 6) {
            if (status == 1) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("V1590.0");
                publishDTO.setAddrv("1");
                res.add(publishDTO);
            }
            if (status == 2) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("V1590.2");
                publishDTO.setAddrv("1");
                res.add(publishDTO);
            }
            if (status == 3) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("V1590.1");
                publishDTO.setAddrv("1");
                res.add(publishDTO);
            }
        }
        return res;
    }

    /**
     * 放置于下发指令之前 给之前的指令进行初始化 0 操作
     * 1（V1090） 2（V1190） 3（V1290） 4（V1390） 5（V4090）   .2 停止  .1 下降 .0 上升  addr 传递括号中数字 addrv 传递 .2 .1 .0
     *
     * @param status 1开 2停 3关
     * @param num
     * @param status
     * @param pid
     * @return
     */
    public List<PublishDTO> getInit01CommendFor4(int num, Integer status, String pid) {
        List<PublishDTO> res = new ArrayList<>();

        if (num == 1) {
            PublishDTO publishDTO = new PublishDTO();
            publishDTO.setPid(pid);
            publishDTO.setAddr("V1090.0");
            publishDTO.setAddrv("0");
            res.add(publishDTO);


            PublishDTO publishDTO2 = new PublishDTO();
            publishDTO2.setPid(pid);
            publishDTO2.setAddr("V1090.2");
            publishDTO2.setAddrv("0");
            res.add(publishDTO2);


            PublishDTO publishDTO3 = new PublishDTO();
            publishDTO3.setPid(pid);
            publishDTO3.setAddr("V1090.1");
            publishDTO3.setAddrv("0");
            res.add(publishDTO3);


        }
        if (num == 2) {

            PublishDTO publishDTO = new PublishDTO();
            publishDTO.setPid(pid);
            publishDTO.setAddr("V1190.0");
            publishDTO.setAddrv("0");
            res.add(publishDTO);


            PublishDTO publishDTO2 = new PublishDTO();
            publishDTO2.setPid(pid);
            publishDTO2.setAddr("V1190.2");
            publishDTO2.setAddrv("0");

            res.add(publishDTO2);


            PublishDTO publishDTO3 = new PublishDTO();
            publishDTO3.setPid(pid);
            publishDTO3.setAddr("V1190.1");
            publishDTO3.setAddrv("0");

            res.add(publishDTO3);

        }
        if (num == 3) {

            PublishDTO publishDTO = new PublishDTO();
            publishDTO.setPid(pid);
            publishDTO.setAddr("V1290.0");
            publishDTO.setAddrv("0");
            res.add(publishDTO);

            PublishDTO publishDTO2 = new PublishDTO();
            publishDTO2.setPid(pid);
            publishDTO2.setAddr("V1290.2");
            publishDTO2.setAddrv("0");
            res.add(publishDTO2);

            PublishDTO publishDTO3 = new PublishDTO();
            publishDTO3.setPid(pid);
            publishDTO3.setAddr("V1290.1");
            publishDTO3.setAddrv("0");
            res.add(publishDTO3);

        }
        if (num == 4) {

            PublishDTO publishDTO = new PublishDTO();
            publishDTO.setPid(pid);
            publishDTO.setAddr("V1390.0");
            publishDTO.setAddrv("0");
            res.add(publishDTO);


            PublishDTO publishDTO2 = new PublishDTO();
            publishDTO2.setPid(pid);
            publishDTO2.setAddr("V1390.2");
            publishDTO2.setAddrv("0");
            res.add(publishDTO2);


            PublishDTO publishDTO3 = new PublishDTO();
            publishDTO3.setPid(pid);
            publishDTO3.setAddr("V1390.1");
            publishDTO3.setAddrv("0");
            res.add(publishDTO3);

        }
        if (num == 5) {

            PublishDTO publishDTO = new PublishDTO();
            publishDTO.setPid(pid);
            publishDTO.setAddr("V1490.0");
            publishDTO.setAddrv("0");
            res.add(publishDTO);

            PublishDTO publishDTO2 = new PublishDTO();
            publishDTO2.setPid(pid);
            publishDTO2.setAddr("V1490.2");
            publishDTO2.setAddrv("0");
            res.add(publishDTO2);

            PublishDTO publishDTO3 = new PublishDTO();
            publishDTO3.setPid(pid);
            publishDTO3.setAddr("V1490.1");
            publishDTO3.setAddrv("0");
            res.add(publishDTO3);

        }
        if (num == 6) {
            if (status == 1) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("V1590.0");
                publishDTO.setAddrv("0");
                res.add(publishDTO);
            }
            if (status == 2) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("V1590.2");
                publishDTO.setAddrv("0");
                res.add(publishDTO);
            }
            if (status == 3) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("V1590.1");
                publishDTO.setAddrv("0");
                res.add(publishDTO);
            }
        }
        return res;
    }


    /**
     * 复位指令
     *
     * @param num
     * @param status
     * @param pid
     * @return
     */
    public List<PublishDTO> getRevertDownCommend(int num, Integer status, String pid) {
        List<PublishDTO> res = new ArrayList<>();

        if (num == 1) {
            if (status == 1) {
                PublishDTO publishDTO2 = new PublishDTO();
                publishDTO2.setPid(pid);
                publishDTO2.setAddr("M0.2");
                publishDTO2.setAddrv("0");
                res.add(publishDTO2);
            }
            if (status == 2) {

                PublishDTO publishDTO2 = new PublishDTO();
                publishDTO2.setPid(pid);
                publishDTO2.setAddr("M0.1");
                publishDTO2.setAddrv("0");
                res.add(publishDTO2);
            }
            if (status == 3) {

                PublishDTO publishDTO2 = new PublishDTO();
                publishDTO2.setPid(pid);
                publishDTO2.setAddr("M0.0");
                publishDTO2.setAddrv("0");
                res.add(publishDTO2);
            }
        }
        if (num == 2) {
            if (status == 1) {
                PublishDTO publishDTO2 = new PublishDTO();
                publishDTO2.setPid(pid);
                publishDTO2.setAddr("M1.0");
                publishDTO2.setAddrv("0");
                res.add(publishDTO2);


            }
            if (status == 2) {
                PublishDTO publishDTO2 = new PublishDTO();
                publishDTO2.setPid(pid);
                publishDTO2.setAddr("M0.7");
                publishDTO2.setAddrv("0");
                res.add(publishDTO2);
            }
            if (status == 3) {

                PublishDTO publishDTO2 = new PublishDTO();
                publishDTO2.setPid(pid);
                publishDTO2.setAddr("M0.6");
                publishDTO2.setAddrv("0");
                res.add(publishDTO2);
            }
        }
        return res;
    }

    /**
     * 复位指令 标段 1
     * @param num
     * @param status
     * @param pid
     * @return
     */
    public List<PublishDTO> getRevertDownCommend1(int num, Integer status, String pid) {
        List<PublishDTO> res = new ArrayList<>();

        if (num == 1) {
            if (status == 1) {
                PublishDTO publishDTO2 = new PublishDTO();
                publishDTO2.setPid(pid);
                publishDTO2.setAddr("M0.0");
                publishDTO2.setAddrv("0");
                res.add(publishDTO2);
            }
            if (status == 2) {

                PublishDTO publishDTO2 = new PublishDTO();
                publishDTO2.setPid(pid);
                publishDTO2.setAddr("M0.1");
                publishDTO2.setAddrv("0");
                res.add(publishDTO2);
            }
            if (status == 3) {

                PublishDTO publishDTO2 = new PublishDTO();
                publishDTO2.setPid(pid);
                publishDTO2.setAddr("M0.2");
                publishDTO2.setAddrv("0");
                res.add(publishDTO2);
            }
        }
        if (num == 2) {
            if (status == 1) {
                PublishDTO publishDTO2 = new PublishDTO();
                publishDTO2.setPid(pid);
                publishDTO2.setAddr("M0.6");
                publishDTO2.setAddrv("0");
                res.add(publishDTO2);
            }
            if (status == 2) {
                PublishDTO publishDTO2 = new PublishDTO();
                publishDTO2.setPid(pid);
                publishDTO2.setAddr("M0.7");
                publishDTO2.setAddrv("0");
                res.add(publishDTO2);
            }
            if (status == 3) {
                PublishDTO publishDTO2 = new PublishDTO();
                publishDTO2.setPid(pid);
                publishDTO2.setAddr("M1.0");
                publishDTO2.setAddrv("0");
                res.add(publishDTO2);
            }
        }
        if (num == 3) {
            if (status == 1) {
                PublishDTO publishDTO2 = new PublishDTO();
                publishDTO2.setPid(pid);
                publishDTO2.setAddr("M1.4");
                publishDTO2.setAddrv("0");
                res.add(publishDTO2);
            }
            if (status == 2) {
                PublishDTO publishDTO2 = new PublishDTO();
                publishDTO2.setPid(pid);
                publishDTO2.setAddr("M1.5");
                publishDTO2.setAddrv("0");
                res.add(publishDTO2);
            }
            if (status == 3) {
                PublishDTO publishDTO2 = new PublishDTO();
                publishDTO2.setPid(pid);
                publishDTO2.setAddr("M1.6");
                publishDTO2.setAddrv("0");
                res.add(publishDTO2);
            }
        }
        return res;
    }

    /**
     * 复位指令 标段 1 特殊处理
     * @param num
     * @param status
     * @param pid
     * @return
     */
    public List<PublishDTO> getRevertDownCommend1Sp(int num, Integer status, String pid) {
        List<PublishDTO> res = new ArrayList<>();

        if (num == 1) {
            if (status == 1) {
                PublishDTO publishDTO2 = new PublishDTO();
                publishDTO2.setPid(pid);
                publishDTO2.setAddr("M0.2");
                publishDTO2.setAddrv("0");
                res.add(publishDTO2);
            }
            if (status == 2) {

                PublishDTO publishDTO2 = new PublishDTO();
                publishDTO2.setPid(pid);
                publishDTO2.setAddr("M0.1");
                publishDTO2.setAddrv("0");
                res.add(publishDTO2);
            }
            if (status == 3) {

                PublishDTO publishDTO2 = new PublishDTO();
                publishDTO2.setPid(pid);
                publishDTO2.setAddr("M0.0");
                publishDTO2.setAddrv("0");
                res.add(publishDTO2);
            }
        }
        if (num == 2) {
            if (status == 1) {
                PublishDTO publishDTO2 = new PublishDTO();
                publishDTO2.setPid(pid);
                publishDTO2.setAddr("M1.0");
                publishDTO2.setAddrv("0");
                res.add(publishDTO2);
            }
            if (status == 2) {
                PublishDTO publishDTO2 = new PublishDTO();
                publishDTO2.setPid(pid);
                publishDTO2.setAddr("M0.7");
                publishDTO2.setAddrv("0");
                res.add(publishDTO2);
            }
            if (status == 3) {
                PublishDTO publishDTO2 = new PublishDTO();
                publishDTO2.setPid(pid);
                publishDTO2.setAddr("M0.6");
                publishDTO2.setAddrv("0");
                res.add(publishDTO2);
            }
        }
        if (num == 3) {
            if (status == 1) {
                PublishDTO publishDTO2 = new PublishDTO();
                publishDTO2.setPid(pid);
                publishDTO2.setAddr("M1.6");
                publishDTO2.setAddrv("0");
                res.add(publishDTO2);
            }
            if (status == 2) {
                PublishDTO publishDTO2 = new PublishDTO();
                publishDTO2.setPid(pid);
                publishDTO2.setAddr("M1.5");
                publishDTO2.setAddrv("0");
                res.add(publishDTO2);
            }
            if (status == 3) {
                PublishDTO publishDTO2 = new PublishDTO();
                publishDTO2.setPid(pid);
                publishDTO2.setAddr("M1.4");
                publishDTO2.setAddrv("0");
                res.add(publishDTO2);
            }
        }
        return res;
    }



}
