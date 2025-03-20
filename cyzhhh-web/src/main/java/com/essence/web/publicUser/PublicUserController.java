package com.essence.web.publicUser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.cache.service.RedisService;
import com.essence.common.constant.ItemConstant;
import com.essence.common.utils.ResponseResult;
import com.essence.common.utils.WxUtils;
import com.essence.dao.StVxInfoDao;
import com.essence.dao.entity.StVxInfoEntity;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.response.EuauthResponse;
import com.essence.euauth.entity.LoginUserDTO;
import com.essence.euauth.entity.PubUserSync;
import com.essence.euauth.entity.ValidResponse;
import com.essence.euauth.feign.AuthValidRemoteFeign;
import com.essence.euauth.feign.UserSyncFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * 群众端 用户登录
 */
@RestController
@RequestMapping("publicUser")
public class PublicUserController {
    @Autowired
    RedisService redisService;

    @Autowired
    @Lazy
    private AuthValidRemoteFeign authValidRemoteFeign;

    @Autowired
    private UserSyncFeign userSyncFeign;

    @Resource
    private StVxInfoDao stVxInfoDao;

    /**
     * 返回系统token  微信 扫码调用该接口 从中获取手机号然后去用户表中查询是否有该用户 如果有的话 则使用账号 密码登录 否则就 注册一个用户然后登录 返回登录的token
     *
     * @return
     */
    @GetMapping("login/token")
    @Transactional
    public ResponseResult getToken(@RequestParam("code") String code,@RequestParam("openid") String openid) {
        WxUtils wxUtils = new WxUtils();

        String phone = (String) wxUtils.getPhone(code);
        //使用手机号 进行用户查询 是否有用户 账号是否唯一 唯一的话 则表示有用户

        //如果不存在则新增用户
//        String baseId = UUID.randomUUID().toString().replace("-", "");
        PubUserSync pubUserSync = new PubUserSync();
        pubUserSync.setUserId(openid);
        pubUserSync.setUserName(openid);
        pubUserSync.setLoginName(openid);
        pubUserSync.setPassword("123qwe!@#");
        Integer isLocked = 0;
        pubUserSync.setIsLocked(isLocked);
        pubUserSync.setMobilephone(phone);
        pubUserSync.setInitPasswdChanged(ItemConstant.USER_PASSSWORD_CHANGED);
        pubUserSync.setCreateTime(new Date());
        userSyncFeign.addSync(pubUserSync);
        //给 维护的vxopenid 新增或者更新 用户
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("openid",openid);
        StVxInfoEntity stVxInfoEntity = stVxInfoDao.selectOne(queryWrapper);

        if (stVxInfoEntity == null){
            stVxInfoEntity = new StVxInfoEntity();
            stVxInfoEntity.setPhone(phone);
            stVxInfoEntity.setOpenid(openid);
            stVxInfoDao.insert(stVxInfoEntity);
        }else {
            stVxInfoDao.update(stVxInfoEntity,queryWrapper);
        }
        //.新增完成再进行登录
        String captcha = userSyncFeign.getCaptcha();
        JSONObject jsonObject = JSON.parseObject(captcha);
        if (jsonObject != null){
            String captchaToken = (String) jsonObject.get("captchaToken");
            //如果存在则进行登录

            redisService.setCacheObject(SysConstant.REDIS_SALT_KEY + captchaToken,123);
            String xor = XOR("78302615c8b79cac8df6d2607f8a83ee", 123);
            LoginUserDTO loginUserDTO = new LoginUserDTO();
            loginUserDTO.setLoginName(openid);
            loginUserDTO.setPassword(xor);
            loginUserDTO.setCaptchaToken(captchaToken);

            EuauthResponse euauthResponse = userSyncFeign.gxLogin(loginUserDTO);
            return ResponseResult.success("查询成功",euauthResponse.getResult());
        }

        return null;
    }


    // 加盐

    /**
     *
     * @param str 密码
     * @param i 盐
     * @return
     */
    private static String XOR(String str, int i) {
        char[] chars = str.toCharArray();
        StringBuffer result = new StringBuffer();
        for (char aChar : chars) {
            result.append(aChar ^ i);
            result.append("_");
        }
        return result.toString();
    }



    @GetMapping("login/openid")
    public ResponseResult getopenid(@RequestParam("loginCode") String loginCode) {
        WxUtils wxUtils = new WxUtils();
        String openid = (String) wxUtils.getOpenid(loginCode);
//        String phone = (String) wxUtils.get
        //使用该Openid 查询是否有该用户信息 如果有的话就登录 没有的话就返回前端没有openid状态
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("openid",openid);
        StVxInfoEntity stVxInfoEntity = stVxInfoDao.selectOne(queryWrapper);
        if (stVxInfoEntity != null){
            String phone = stVxInfoEntity.getPhone();
            String captcha = userSyncFeign.getCaptcha();
            JSONObject jsonObject = JSON.parseObject(captcha);
            if (jsonObject != null){
                String captchaToken = (String) jsonObject.get("captchaToken");
                //如果存在则进行登录
                redisService.setCacheObject(SysConstant.REDIS_SALT_KEY + captchaToken,123);
                String xor = XOR("78302615c8b79cac8df6d2607f8a83ee", 123);
                LoginUserDTO loginUserDTO = new LoginUserDTO();
                loginUserDTO.setLoginName(openid);
                loginUserDTO.setPassword(xor);
                loginUserDTO.setCaptchaToken(captchaToken);

                EuauthResponse euauthResponse = userSyncFeign.gxLogin(loginUserDTO);
                return ResponseResult.success("查询成功",euauthResponse.getResult());
            }
        }else {
            return ResponseResult.error("没有该openidvx用户",openid);
        }
        return ResponseResult.error("查询成功",openid);
    }




}
