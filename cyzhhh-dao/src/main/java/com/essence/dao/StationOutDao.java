package com.essence.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StationOutDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 水位流量站(对外)(StationOut)表数据库访问层
 *
 * @author BINX
 * @since 2023-05-11 10:29:50
 */
@Mapper
public interface StationOutDao extends BaseDao<StationOutDto> {
}
