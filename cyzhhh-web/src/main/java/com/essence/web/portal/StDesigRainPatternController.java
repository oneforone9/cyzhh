package com.essence.web.portal;


import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.StDesigRainPatternService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.FloodDesigRainPatternEsrVo;
import com.essence.interfaces.model.StDesigRainPatternEsr;
import com.essence.interfaces.model.StDesigRainPatternEsu;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;


/**
 * 设计雨型管理
 *
 * @author majunjie
 * @since 2023-04-24 09:57:24
 */

@RestController
@RequestMapping("/stDesigRainPattern")
public class StDesigRainPatternController {
    @Autowired
    private StDesigRainPatternService stDesigRainPatternService;

    /**
     * 雨型信息根据条件分页查询
     *
     * @param param
     * @return R
     */

    @PostMapping("/selectDesigRainPatternList")
    public ResponseResult<Paginator<StDesigRainPatternEsr>> selectDesigRainPatternList(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", stDesigRainPatternService.findByPaginator(param));
    }


    /**
     * 雨型查询(通过预见期)
     *
     * @return R
     */

    @GetMapping("/selectDesigRainPattern/{hourCount}")
    public ResponseResult <List<FloodDesigRainPatternEsrVo>> selectDesigRainPattern(@PathVariable String hourCount) {
        return ResponseResult.success("列表查询成功", stDesigRainPatternService.selectDesigRainPattern(hourCount));
    }


    /**
     * 新增雨型
     *
     * @param stDesigRainPatternEsu
     * @return
     */

    @PostMapping("/addRainFallPattern")
    public ResponseResult addRainFallPattern(HttpServletRequest request, @RequestBody StDesigRainPatternEsu stDesigRainPatternEsu) {
        stDesigRainPatternEsu.setGmtCreate(new Date());
        String s = stDesigRainPatternService.addRainFallPattern(stDesigRainPatternEsu);
        if (StringUtils.isNotBlank(s)) {
            return ResponseResult.success("添加成功", "");
        } else {
            return ResponseResult.error("添加失败");
        }
    }
}
