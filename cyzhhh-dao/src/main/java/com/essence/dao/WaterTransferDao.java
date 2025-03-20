package com.essence.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.WaterTransferDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 调水表(WaterTransfer)表数据库访问层
 *
 * @author BINX
 * @since 2023-05-09 09:53:44
 */
@Mapper
public interface WaterTransferDao extends BaseDao<WaterTransferDto> {
}
