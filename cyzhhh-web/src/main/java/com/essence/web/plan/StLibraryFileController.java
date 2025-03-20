package com.essence.web.plan;

import com.essence.common.utils.ResponseResult;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.StLibraryFileService;
import com.essence.interfaces.model.StLibraryFileEsr;
import com.essence.interfaces.model.StLibraryFileEsu;
import com.essence.interfaces.model.StPlanInfoEsu;
import com.essence.interfaces.param.StLibraryFileEsp;
import com.essence.interfaces.vaild.Insert;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


/**
 * 知识库文件表管理
 * @author liwy
 * @since 2023-08-17 10:21:04
 */
@RestController
@RequestMapping("/stLibraryFile")
public class StLibraryFileController extends BaseController<Long, StLibraryFileEsu, StLibraryFileEsp, StLibraryFileEsr> {
    @Autowired
    private StLibraryFileService stLibraryFileService;

    public StLibraryFileController(StLibraryFileService stLibraryFileService) {
        super(stLibraryFileService);
    }

    /**
     * 新增知识库文件
     *
     * @param stLibraryFileEsu
     * @return
     */
    @PostMapping("/addStLibraryFile")
    @ResponseBody
    public ResponseResult addStLibraryFile(HttpServletRequest request,  @RequestBody StLibraryFileEsu stLibraryFileEsu) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        String userName = (String) request.getSession().getAttribute(SysConstant.CURRENT_USERNAME);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-新增失败");
        }
        stLibraryFileEsu.setUserId(userId);
        stLibraryFileEsu.setUserName(userName);
        stLibraryFileEsu.setUploadTime(new Date());
        return ResponseResult.success("添加成功", stLibraryFileService.insert(stLibraryFileEsu));

    }

    /**
     * 修改知识库文件
     * @param stLibraryFileEsu
     * @return
     */
    @PostMapping("/updateStLibraryFile")
    @ResponseBody
    public ResponseResult updateStLibraryFile(HttpServletRequest request,  @RequestBody StLibraryFileEsu stLibraryFileEsu) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        String userName = (String) request.getSession().getAttribute(SysConstant.CURRENT_USERNAME);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-更新失败");
        }
        stLibraryFileEsu.setUserId(userId);
        stLibraryFileEsu.setUserName(userName);
        stLibraryFileEsu.setUploadTime(new Date());
        return ResponseResult.success("修改成功", stLibraryFileService.update(stLibraryFileEsu));

    }
}
