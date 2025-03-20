package com.essence.web.basecontroller;

import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.entity.*;
import com.essence.interfaces.vaild.Insert;
import com.essence.interfaces.vaild.Update;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * @author zhy
 * @date 2021/12/9 16:53
 * @Description ID 主键,E 更新实体,P 查询条件实体,R 返回结果实体
 */

public abstract class BaseController<ID, E extends Esu, P extends Esp, R extends Esr> {
    private BaseApi<E, P, R> baseApi;

    public BaseController(BaseApi<E, P, R> baseApi) {
        this.baseApi = baseApi;
    }

    /**
     * 新增
     *
     * @param e
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResponseResult insert(@Validated(Insert.class) @RequestBody E e) {
        return ResponseResult.success("添加成功", baseApi.insert(e));

    }

    /**
     * 更新
     *
     * @param e
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public ResponseResult update(@Validated(Update.class) @RequestBody E e) {
        return ResponseResult.success("更新成功", baseApi.update(e));
    }

    /**
     * 删除
     *
     * @param id 主键
     * @return
     */
    @GetMapping("/delete/{id}")
    @ResponseBody
    public ResponseResult delete(@PathVariable ID id) {
        return ResponseResult.success("删除成功", baseApi.deleteById((Serializable) id));
    }

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return R
     */
    @GetMapping("/search/{id}")
    @ResponseBody
    public ResponseResult<R> search(@PathVariable ID id) {
        return ResponseResult.success("查询成功", baseApi.findById((Serializable) id));
    }

    /**
     * 根据条件分页查询
     *
     * @param param
     * @return Paginator<R>
     */
    @PostMapping("/search")
    @ResponseBody
    public ResponseResult<Paginator<R>> search(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", baseApi.findByPaginator(param));
    }
}
