package com.essence.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.constant.WxMessageparams;
import com.essence.common.utils.HttpUtils;
import com.essence.common.utils.TimeUtil;
import com.essence.common.utils.WxMessageUtil;
import com.essence.dao.StOpenBaseDao;
import com.essence.dao.entity.StOpenBaseDto;
import com.essence.interfaces.api.StOpenBaseService;
import com.essence.interfaces.model.StOpenBaseEsr;
import com.essence.interfaces.model.StOpenBaseEsu;
import com.essence.interfaces.model.WorkorderBaseEsu;
import com.essence.interfaces.param.StOpenBaseEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStOpenBaseEtoT;
import com.essence.service.converter.ConverterStOpenBaseTtoR;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * (StOpenBase)业务层
 * @author liwy
 * @since 2023-08-23 14:28:18
 */
@Slf4j
@Service
public class StOpenBaseServiceImpl extends BaseApiImpl<StOpenBaseEsu, StOpenBaseEsp, StOpenBaseEsr, StOpenBaseDto> implements StOpenBaseService {
    //微信小程序APPID
    @Value("${msg.AppID}")
    private String AppID;
    //微信小程序密钥
    @Value("${msg.AppSecret}")
    private String AppSecret;
    //模板ID
    @Value("${msg.tmplIds}")
    private String tmplIds;
    //跳转页面
    @Value("${msg.page}")
    private String page;
    //发送url
    @Value("${msg.sendUrl}")
    private String sendUrl;
    //tokenUr
    @Value("${msg.tokenUrl}")
    private String tokenUrl;


    @Autowired
    private StOpenBaseDao stOpenBaseDao;
    @Autowired
    private ConverterStOpenBaseEtoT converterStOpenBaseEtoT;
    @Autowired
    private ConverterStOpenBaseTtoR converterStOpenBaseTtoR;
    @Autowired
    private StOpenBaseService stOpenBaseService;

    public StOpenBaseServiceImpl(StOpenBaseDao stOpenBaseDao, ConverterStOpenBaseEtoT converterStOpenBaseEtoT, ConverterStOpenBaseTtoR converterStOpenBaseTtoR) {
        super(stOpenBaseDao, converterStOpenBaseEtoT, converterStOpenBaseTtoR);
    }

    /**
     * 根据jsCode获取openId并绑定当前登录的用户
     * @param request
     * @param stOpenBaseEsu
     * @return
     */
    @Override
    public Integer getOpenId(HttpServletRequest request, StOpenBaseEsu stOpenBaseEsu) throws Exception {
        Integer insert = 0;
        String jsCode = stOpenBaseEsu.getJsCode();
//        String AppID = "wxc6d24e9bf7719872";
//        String AppSecret = "846661090d628733f066840b689dba68";
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+AppID+"&secret="+AppSecret+"&js_code="+jsCode+"&grant_type=authorization_code";
        String s = HttpUtils.doGet(url);
        JSONObject json = (JSONObject) JSONObject.parse(s);
        String openid = (String) json.get("openid");
        log.info(s);
        log.info(openid);

        stOpenBaseEsu.setOpenId(openid);
        stOpenBaseEsu.setGmtCreate(new Date());
        //先判断用户userId是否已经绑定
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",stOpenBaseEsu.getUserId());
        List<StOpenBaseDto> list = stOpenBaseDao.selectList(queryWrapper);

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("open_id",openid);
        List<StOpenBaseDto> list2 = stOpenBaseDao.selectList(wrapper);

        if(list.size()>0){
            log.info("当前用户已绑定微信，不能重复绑定");
        }else{
            if(list2.size()>0){
                log.info("当前微信已绑定用户，不能再绑定");
            }else{
                insert = stOpenBaseService.insert(stOpenBaseEsu);
            }

        }
        return insert;
    }

    /**
     * 获取当前登录的用户openId。 true表示已经，false表示未绑定过
     * @param userId
     * @return
     */
    @Override
    public Boolean selectOpenId(String userId) {
        //先判断用户userId是否已经绑定
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",userId);
        List<StOpenBaseDto> list = stOpenBaseDao.selectList(queryWrapper);
        if(list.size()>0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 订阅消息通知
     * @return
     * @param workorderBaseEsu
     */
    @Override
    public String sendMsg(WorkorderBaseEsu workorderBaseEsu) {
        //获取到openid
        String openId = null;
        //String openId = "ocvzB5PpQejJo0VSWqNPzye-dak0";
        String userId = workorderBaseEsu.getPersonId();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",userId);
        List<StOpenBaseDto> list = stOpenBaseDao.selectList(queryWrapper);
        if(list.size()>0){
            openId = list.get(0).getOpenId();
        }else{
            log.info("未找到的对应的openId。");
        }

        if(openId != null && !"".equals(openId)){
            WxMessageparams wxMessageparams = new WxMessageparams();
            wxMessageparams.setTouser(openId);
            wxMessageparams.setTemplate_id(tmplIds);
            wxMessageparams.setPage(page);
            /**
             * 跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
             */
            wxMessageparams.setMiniprogram_state("trial");

//        Map<String, String> obj = new HashMap<>();
//        String time = TimeUtil.dateToStringNormal(new Date());
//        obj.put("value", time);
//        Map<String, String> obj2 = new HashMap<>();
//        obj2.put("value", "工单名称01");
//        Map<String, String> obj3 = new HashMap<>();
//        obj3.put("value", "巡查工单");
//        Map<String, String> obj4 = new HashMap<>();
//        obj4.put("value", "534563463564");

            Map<String, String> obj = new HashMap<>();
            //obj.put("value", workorderBaseEsu.getStartTime()==null?"":workorderBaseEsu.getStartTime().toString());
            String time = TimeUtil.dateToStringNormal(new Date());
            obj.put("value", time);
            Map<String, String> obj2 = new HashMap<>();
            obj2.put("value", workorderBaseEsu.getOrderName());
            Map<String, String> obj3 = new HashMap<>();
            obj3.put("value", workorderBaseEsu.getOrderType());
            Map<String, String> obj4 = new HashMap<>();
            obj4.put("value", workorderBaseEsu.getOrderCode());

            Map<String, Map> objMap = new HashMap<>();
            objMap.put("time7", obj);
            objMap.put("thing1", obj2);
            objMap.put("thing4", obj3);
            objMap.put("character_string10", obj4);
            wxMessageparams.setData(objMap);
            String s = WxMessageUtil.sendMsg(wxMessageparams, AppID, AppSecret, sendUrl, tokenUrl);
            log.info("ssssssss"+s);
            return s;
        }else{
            log.info("未找到的对应的openId。");
        }
        return "未找到的对应的openId";








    }

}
