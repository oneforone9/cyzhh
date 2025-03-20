package com.essence.web.data;


import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.SectionDataViewService;
import com.essence.interfaces.model.SectionDataViewEsr;
import com.essence.interfaces.model.SectionDataViewEsu;
import com.essence.interfaces.param.SectionDataViewEsp;
import com.essence.interfaces.vaild.Insert;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 断面台帐数据
 * @author BINX
 * @since 2023-01-05 14:59:46
 */
@RestController
@RequestMapping("/sectionDataView")
public class SectionDataViewController extends BaseController<String, SectionDataViewEsu, SectionDataViewEsp, SectionDataViewEsr> {
    @Autowired
    private SectionDataViewService sectionDataViewService;

    public SectionDataViewController(SectionDataViewService sectionDataViewService) {
        super(sectionDataViewService);
    }

    @Override
    @PostMapping({"/add"})
    public ResponseResult insert(@RequestBody SectionDataViewEsu esu) {
        return ResponseResult.success("添加成功", this.sectionDataViewService.insert(esu));
    }

    @Override
    @PostMapping({"/update"})
    public ResponseResult update(@RequestBody SectionDataViewEsu esu) {
        return ResponseResult.success("更新成功", this.sectionDataViewService.update(esu));
    }

    @Override
    @GetMapping({"/delete/{id}"})
    public ResponseResult delete(@PathVariable String id) {
        return ResponseResult.success("删除成功", this.sectionDataViewService.deleteById(id));
    }


}
