package com.essence.service.impl.third;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.StThirdUserInfoDao;
import com.essence.dao.StThirdUserRelationDao;
import com.essence.dao.entity.StThirdUserInfoDto;
import com.essence.dao.entity.StThirdUserRelationDto;
import com.essence.eluban.utils.AesUtils;
import com.essence.interfaces.api.third.StThirdUserInfoService;
import com.essence.interfaces.model.StThirdUserInfoEsr;
import com.essence.interfaces.model.StThirdUserInfoEsu;
import com.essence.interfaces.param.StThirdUserInfoEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStThirdUserInfoEtoT;
import com.essence.service.converter.ConverterStThirdUserInfoTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对接第三方用户基本信息 单点登录(StThirdUserInfo)业务层
 * @author BINX
 * @since 2023-04-03 14:31:20
 */
@Service
public class StThirdUserInfoServiceImpl extends BaseApiImpl<StThirdUserInfoEsu, StThirdUserInfoEsp, StThirdUserInfoEsr, StThirdUserInfoDto> implements StThirdUserInfoService {


    /**
     * //aes 密钥 (需要前端和后端保持一致)
     * 北京水资源
     */
    public static final String BJSZY = "sence$cyzhhh@szy";
    /**
     * 北京理正
     */
    public static final String LZKEY = "essencecyzhhh@lz";

    /**
     * 节水平台
     */
    public static final String JSPT = "essencecyzhhh@js";
    //aes 算法
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    static Map<String,String> companyKey = null;
    static {
        companyKey = new HashMap<>();
        companyKey.put("BJSZY",BJSZY);
        companyKey.put("LZKEY",LZKEY);
        companyKey.put("JSPT",JSPT);

    }

    @Resource
    private StThirdUserRelationDao thirdUserRelationDao;
    @Autowired
    private StThirdUserInfoDao stThirdUserInfoDao;
    @Autowired
    private ConverterStThirdUserInfoEtoT converterStThirdUserInfoEtoT;
    @Autowired
    private ConverterStThirdUserInfoTtoR converterStThirdUserInfoTtoR;

    public StThirdUserInfoServiceImpl(StThirdUserInfoDao stThirdUserInfoDao, ConverterStThirdUserInfoEtoT converterStThirdUserInfoEtoT, ConverterStThirdUserInfoTtoR converterStThirdUserInfoTtoR) {
        super(stThirdUserInfoDao, converterStThirdUserInfoEtoT, converterStThirdUserInfoTtoR);
    }

    @Override
    public void addOrUpdateUserInfo(StThirdUserInfoEsu stThirdUserInfoEsu) {
        QueryWrapper wrapper = new QueryWrapper();
        StThirdUserInfoDto stThirdUserInfoDto = new StThirdUserInfoDto();
        BeanUtil.copyProperties(stThirdUserInfoEsu,stThirdUserInfoDto);
        wrapper.eq("user_id",stThirdUserInfoDto.getUserId());
        List<StThirdUserInfoDto> stThirdUserInfoDtos = stThirdUserInfoDao.selectList(wrapper);
        if (CollUtil.isEmpty(stThirdUserInfoDtos)){
            stThirdUserInfoDao.insert(stThirdUserInfoDto);
        }else {
            stThirdUserInfoDao.update(stThirdUserInfoDto,wrapper);
        }
    }

    @Override
    public void deleteByThirdUserId(String thirdUserId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id",thirdUserId);
        int delete = stThirdUserInfoDao.delete(wrapper);
    }

    @Override
    public String getThirdUserIdAes(String userId,String companyName) throws Exception {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id",userId);
        StThirdUserRelationDto stThirdUserRelationDto = thirdUserRelationDao.selectOne(wrapper);
        if (stThirdUserRelationDto != null){
            String key = companyKey.get(companyName);
            String thirdUserId = stThirdUserRelationDto.getThirdUserId();
            String encrypt = AesUtils.aesEncrypt(thirdUserId, key);
            System.out.println("加密后：" + encrypt);
            String decrypt = AesUtils.aesDecrypt(encrypt, key);
            System.out.println("解密后：" + decrypt);
            return encrypt;
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        String encrypt = AesUtils.aesEncrypt("23130f35ccf04fa28cdf058b7d67a03b", "sence$cyzhhh@szy");
        System.out.println("加密后：" + encrypt);
        String decrypt = AesUtils.aesDecrypt(encrypt, "sence$cyzhhh@szy");
        System.out.println("解密后：" + decrypt);
    }
}
