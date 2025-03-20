package com.essence.web.plan;

import com.essence.common.utils.ResponseResult;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.StOpenBaseService;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.StOpenBaseEsp;
import com.essence.web.basecontroller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 管理
 * @author liwy
 * @since 2023-08-23 14:28:08
 */
@Slf4j
@RestController
@RequestMapping("/stOpenBase")
public class StOpenBaseController extends BaseController<Long, StOpenBaseEsu, StOpenBaseEsp, StOpenBaseEsr> {

    @Autowired
    private StOpenBaseService stOpenBaseService;

    public StOpenBaseController(StOpenBaseService stOpenBaseService) {
        super(stOpenBaseService);
    }


    /**
     * 根据jsCode获取openId并绑定当前登录的用户
     * @param request
     * @return
     */
    @PostMapping("getOpenId")
    public ResponseResult getOpenId(HttpServletRequest request, @RequestBody StOpenBaseEsu stOpenBaseEsu) throws Exception {
          String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-添加失败");
        }
        //userId="111";
        stOpenBaseEsu.setUserId(userId);
        Integer insert = stOpenBaseService.getOpenId(request,stOpenBaseEsu);
        return ResponseResult.success("添加成功", insert);
    }


    /**
     * 订阅消息通知
     * @throws Exception
     * @return
     */
    @PostMapping("sendMsg")
    public String sendMsg(HttpServletRequest request,@RequestBody WorkorderBaseEsu workorderBaseEsu){
        //微信小程序APPID
//        String AppID = "wxc6d24e9bf7719872";
//         //微信小程序密钥
//        String AppSecret = "846661090d628733f066840b689dba68";
//        String sendUrl = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=";
//        String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token";
//        String tmplIds = "jwbUi-jy0MK-CfyyuUgHl1oA5GGnjgqtwowKwiue1fc";
//        String page = "pages/text/index";
        String s = stOpenBaseService.sendMsg(workorderBaseEsu);
        return s;

    }


    /**
     * 获取当前登录的用户openId。 true表示已经，false表示未绑定过
     * @param request
     * @return
     */
    @GetMapping("selectOpenId")
    public ResponseResult selectOpenId(HttpServletRequest request) throws Exception {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-获取当前登录订阅失败");
        }
        Boolean b = stOpenBaseService.selectOpenId(userId);
        return ResponseResult.success("获取当前登录订阅成功！", b);
    }



}
