package com.essence.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StGaConvertDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 闸坝泵id 和 采集sn码转换对接一下(StGaConvert)表数据库访问层
 *
 * @author BINX
 * @since 2023-05-25 18:06:35
 */
@Mapper
public interface StGaConvertDao extends BaseDao<StGaConvertDto> {
}
