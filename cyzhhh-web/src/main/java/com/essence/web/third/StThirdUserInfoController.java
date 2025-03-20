package com.essence.web.third;


import cn.hutool.core.util.StrUtil;
import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.third.StThirdUserInfoService;
import com.essence.interfaces.model.StThirdUserInfoEsr;
import com.essence.interfaces.model.StThirdUserInfoEsu;
import com.essence.interfaces.param.StThirdUserInfoEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

/**
 * 对接第三方用户基本信息 单点登录管理
 *
 * @author BINX
 * @since 2023-04-03 14:31:18
 */
@RestController
@RequestMapping("/third/sso")
public class StThirdUserInfoController extends BaseController<String,StThirdUserInfoEsu, StThirdUserInfoEsp, StThirdUserInfoEsr> {
    @Autowired
    private StThirdUserInfoService stThirdUserInfoService;


    @Value("${third.tokens}")
    private String thirdTokens;


    public StThirdUserInfoController(StThirdUserInfoService stThirdUserRelationService) {
        super(stThirdUserRelationService);
    }
    /**
     * 三方用户的新增和修改
     *
     * @return
     */

    @PostMapping("/insert")
    public ResponseResult addOrUpdateUserInfo(@RequestBody StThirdUserInfoEsu stThirdUserInfoEsu, HttpServletRequest request) {
        try {
            String[] split = thirdTokens.split(",");
            String thirdToken = request.getHeader("thirdToken");
            String company= "";
            //循环过滤
            List<String> strings = Arrays.asList(split);
            boolean auth = false;
            for (String authKey : strings) {
                auth = AuthThird.auth(thirdToken,authKey);
                if (auth){
                    company = authKey;
                    break;
                }
            }
            if (auth){
                stThirdUserInfoEsu.setCompany(company);
                stThirdUserInfoService.addOrUpdateUserInfo(stThirdUserInfoEsu);
            }else {
                return ResponseResult.success("thirdToken过期或者失效",null);
            }
        } catch (Exception e) {
            return ResponseResult.success("新增或者更新失败", e.getStackTrace());
        }
        return ResponseResult.success("新增或者更新成功", null);
    }


    @GetMapping("/delete")
    public ResponseResult deleteThirdUserId(String thirdUserId, HttpServletRequest request) {

        try {
            String[] split = thirdTokens.split(",");
            String thirdToken = request.getHeader("thirdToken");
            String company= "";
            //循环过滤
            List<String> strings = Arrays.asList(split);
            boolean auth = false;
            for (String authKey : strings) {
                auth = AuthThird.auth(thirdToken,authKey);
                if (auth){
                    company = authKey;
                    break;
                }
            }
            if (auth){
                stThirdUserInfoService.deleteByThirdUserId(thirdUserId);
            }else {
                return ResponseResult.success("thirdToken过期或者失效",null);
            }
        } catch (Exception e) {
            return ResponseResult.success("删除失败", null);
        }
        return ResponseResult.success("删除成功", null);

    }


    @GetMapping("/token")
    public ResponseResult getThirdKey(String sign) {
        String bjlz = AuthThird.sign(sign, "123qwe");
        return ResponseResult.success("获取token成功", bjlz);
    }

    /**
     * 获取 加密后的 第三方用户id
     * @param companyName 公司名称
     * @param userId 用户id
     * @return companyName 要跳转的公司名称
     */
    @GetMapping("/aes/userid")
    public ResponseResult getThirdUserIdAES( HttpSession session,String companyName,String userId) throws Exception {

        String thirdUserId = stThirdUserInfoService.getThirdUserIdAes(userId,companyName);
        if (StrUtil.isEmpty(thirdUserId)){
            return ResponseResult.error("获取第三方userId失败 thirdUserId为空，原因可能是未配置当前用户与第三方用户");
        }else {
            return ResponseResult.success("获取第三方userId成功", thirdUserId);
        }
    }
}
