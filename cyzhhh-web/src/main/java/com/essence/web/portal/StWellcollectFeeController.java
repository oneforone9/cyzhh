package com.essence.web.portal;


import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.StWellcollectFeeService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.StWellcollectFeeEsr;
import com.essence.interfaces.model.StWellcollectFeeEsu;
import com.essence.interfaces.model.StWellcollectFeeEsuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 机井管理
 *
 * @author majunjie
 * @since 2022-10-20 14:20:48
 */
@RestController
@RequestMapping("/stWellcollectFee")
public class StWellcollectFeeController {
    @Autowired
    private StWellcollectFeeService stWellcollectFeeService;

    /**
     * 统计分析机井管理
     *
     * @param year
     * @return R
     */
    @GetMapping("/selectStWellcollectFee/{year}")
    public ResponseResult<StWellcollectFeeEsuVo> selectStWellcollectFee(@PathVariable("year") String year) {
        return stWellcollectFeeService.selectStWellcollectFee(year);
    }

    /**
     * 统计分析机井管理根据条件分页查询
     *
     * @param param
     * @return R
     */
    @PostMapping("/selectStWellcollectFeeList")
    public ResponseResult<Paginator<StWellcollectFeeEsr>> selectStWellcollectFeeList(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", stWellcollectFeeService.findByPaginator(param));
    }

    /**
     * 删除机井管理
     *
     * @param id
     * @return
     */
    @GetMapping("/deleteStWellcollectFeeList/{id}")
    public ResponseResult deleteStWellcollectFeeList(HttpServletRequest request, @PathVariable Integer id) {
        return ResponseResult.success("删除成功", stWellcollectFeeService.deleteById(id));
    }

    /**
     * 新增修改机井管理
     *
     * @param stWellcollectFeeEsu
     * @return
     */
    @PostMapping("/addStWellcollectFeeList")
    public ResponseResult addStWellcollectFeeList(HttpServletRequest request, @RequestBody StWellcollectFeeEsu stWellcollectFeeEsu) {
        if (null != stWellcollectFeeEsu.getId()) {
            return ResponseResult.success("修改成功", stWellcollectFeeService.update(stWellcollectFeeEsu));
        } else {
            return ResponseResult.success("添加成功", stWellcollectFeeService.insert(stWellcollectFeeEsu));
        }
    }

}
