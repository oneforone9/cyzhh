package com.essence.web.portal;

import com.essence.common.constant.ItemConstant;
import com.essence.common.utils.ResponseResult;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.util.StringUtil;
import com.essence.framework.util.FileUtil;
import com.essence.interfaces.api.FileBaseService;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.FileBaseEsr;
import com.essence.interfaces.model.FileBaseEsu;
import com.essence.interfaces.param.FileBaseEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件管理表管理
 *
 * @author zhy
 * @since 2022-10-28 16:20:14
 */
@Controller
@RequestMapping("/file")
public class FileBaseController  {
    @Autowired
    private FileBaseService fileBaseService;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ResponseBody
    public ResponseResult<FileBaseEsr> upload(@RequestParam("file") MultipartFile file) {
        return ResponseResult.success("上传成功", fileBaseService.upload(file));
    }


    /**
     * 文件上传 （增加水印）
     * @param file
     * @return
     */
    @PostMapping("/upload/{waterMark}")
    @ResponseBody
    public ResponseResult<FileBaseEsr> upload(@RequestParam("file") MultipartFile file,@PathVariable String waterMark) {
        return ResponseResult.success("上传成功", fileBaseService.uploadForWatermark(file,waterMark));
    }


    /**
     * 文件批量上传
     * @param files
     * @return
     */
    @PostMapping("/uploads")
    @ResponseBody
    public ResponseResult<List<FileBaseEsr>> uploads(@RequestParam("files") MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return ResponseResult.error("未接收到文件");
        }
        List<FileBaseEsr> results = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue; // 跳过空文件
                }
                FileBaseEsr result = fileBaseService.upload(file);
                results.add(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 记录日志
            return ResponseResult.error("文件上传失败");
        }
        return ResponseResult.success("批量上传成功", results);
    }

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    @GetMapping("/search/{id}")
    @ResponseBody
    public ResponseResult<FileBaseEsr> search(@PathVariable String id) {
        return ResponseResult.success("查询成功", fileBaseService.findById(id));
    }

    /**
     * 根据条件分页查询
     *
     * @param param
     * @return
     */
    @PostMapping("/search")
    @ResponseBody
    public ResponseResult<Paginator<FileBaseEsr>> search(@RequestBody PaginatorParam param) {
        List<Criterion> currency = param.getCurrency();
        if (CollectionUtils.isEmpty(currency)){
            currency = new ArrayList<>();
        }
        Criterion criterion = new Criterion();
        criterion.setFieldName("isDelete");
        criterion.setOperator(Criterion.EQ);
        criterion.setValue(ItemConstant.FILE_NO_DELETE);
        currency.add(criterion);
        param.setCurrency(currency);
        return ResponseResult.success("查询成功", fileBaseService.findByPaginator(param));
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    @ResponseBody
    public ResponseResult delete(@PathVariable String id) {

        return ResponseResult.success("删除成功", fileBaseService.deleteById(id));
    }


    /**
     * 根据主键浏览
     * @param id
     * @throws Exception
     */
    @GetMapping("/preview/{id}")
    @ResponseBody
    public void preview(HttpServletRequest request, HttpServletResponse response,@PathVariable(value = "id") String id) throws Exception {
        fileBaseService.previewFile(request, response,id, null);
    }

    /**
     * 根据主键浏览-缩略图
     * @param id
     * @param size
     * @throws Exception
     */
    @GetMapping("/preview/{id}/{size}")
    @ResponseBody
    public void preview(HttpServletRequest request, HttpServletResponse response,@PathVariable(value = "id") String id, @PathVariable(value = "size") String size) throws Exception {
         fileBaseService.previewFile(request, response,id, size);
    }

}
