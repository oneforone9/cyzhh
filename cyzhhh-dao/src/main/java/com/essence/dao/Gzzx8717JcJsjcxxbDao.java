package com.essence.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.essence.dao.basedao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import com.essence.dao.entity.Gzzx8717JcJsjcxxbDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * (Gzzx8717JcJsjcxxb)表数据库访问层
 *
 * @author BINX
 * @since 2023-05-23 17:40:04
 */
@Mapper
@DS("ruler")
public interface Gzzx8717JcJsjcxxbDao extends BaseDao<Gzzx8717JcJsjcxxbDto> {
}
