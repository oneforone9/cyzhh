package com.essence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.utils.CodeGenerateUtil;
import com.essence.dao.CodeGenerateDao;
import com.essence.dao.entity.CodeGenerate;
import com.essence.framework.util.DateUtil;
import com.essence.interfaces.api.CodeGenerateService;
import com.essence.service.utils.OrderCodeGenerateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhy
 * @since 2022/10/27 17:03
 */
@Service
public class CodeGenerateServiceImpl implements CodeGenerateService {

    @Autowired
    private CodeGenerateDao codeGenerateDao;

    @Override
    public List<String> getCode(String key, int num) {
        QueryWrapper<CodeGenerate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code_key", key);
        queryWrapper.eq("tm", DateUtil.dateToStringDay(new Date()));
        List<CodeGenerate> codeGenerates = codeGenerateDao.selectList(queryWrapper);
        CodeGenerate update = null;
        if (!CollectionUtils.isEmpty(codeGenerates)){
            CodeGenerate codeGenerate = codeGenerates.get(0);
            update = codeGenerate;
            CodeGenerateUtil.put(codeGenerate.getCodeKey(), codeGenerate.getCodeValue());
        }
        List<String> resultList = new ArrayList<>();
        int value = -1;
        for (int i = 0; i < num; i++) {
            value = CodeGenerateUtil.get(key);
            resultList.add(key + OrderCodeGenerateUtil.toSequenceCode(value));
        }




        if (null == update){
            update = new CodeGenerate();
            update.setCodeKey(key);
            update.setCodeValue(value);
            update.setTm(DateUtil.dateToStringDay(new Date()));
            codeGenerateDao.insert(update);
        }else{
            update.setCodeValue(value);
            codeGenerateDao.updateById(update);
        }
        return resultList;
    }

    @Override
    public void clearCode(Date date) {
        QueryWrapper<CodeGenerate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tm", DateUtil.dateToStringDay(new Date()));
        codeGenerateDao.delete(queryWrapper);
    }
}
