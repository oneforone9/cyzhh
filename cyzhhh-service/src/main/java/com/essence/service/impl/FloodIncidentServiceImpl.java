package com.essence.service.impl;

import com.essence.common.exception.BusinessException;
import com.essence.dao.FloodIncidentDao;
import com.essence.dao.FloodIncidentRecordDao;
import com.essence.dao.entity.FloodIncident;
import com.essence.dao.entity.FloodIncidentRecord;
import com.essence.interfaces.api.FloodIncidentService;
import com.essence.interfaces.model.FloodIncidentEsr;
import com.essence.interfaces.model.FloodIncidentEsu;
import com.essence.interfaces.param.FloodIncidentEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterFloodIncidentEtoT;
import com.essence.service.converter.ConverterFloodIncidentTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhy
 * @since 2024-07-17 19:32:43
 */
@Service
public class FloodIncidentServiceImpl extends BaseApiImpl<FloodIncidentEsu, FloodIncidentEsp, FloodIncidentEsr, FloodIncident> implements FloodIncidentService {

    @Autowired
    private FloodIncidentDao floodIncidentDao;

    @Autowired
    private FloodIncidentRecordDao floodIncidentRecordDao;

    @Autowired
    private ConverterFloodIncidentEtoT converterFloodIncidentEtoT;

    @Autowired
    private ConverterFloodIncidentTtoR converterFloodIncidentTtoR;

    public FloodIncidentServiceImpl(FloodIncidentDao floodIncidentDao, ConverterFloodIncidentEtoT converterFloodIncidentEtoT, ConverterFloodIncidentTtoR converterFloodIncidentTtoR) {
        super(floodIncidentDao, converterFloodIncidentEtoT, converterFloodIncidentTtoR);
    }


    @Override
    @Transactional
    public int insert(FloodIncidentEsu floodIncidentEsu) {
        String operate;
        if (floodIncidentEsu.getFloodIncidentType() == 0) {
            floodIncidentEsu.setStatus(0);
            floodIncidentEsu.setApplicationUserId(floodIncidentEsu.getOperateUserId());
            floodIncidentEsu.setApplicationUserName(floodIncidentEsu.getOperateUserName());
            operate = "申请";
        } else {
            floodIncidentEsu.setStatus(1);
            operate = "下发";
        }
        // 保存主表
        FloodIncident saveVO = converterFloodIncidentEtoT.toBean(floodIncidentEsu);
        floodIncidentDao.insert(saveVO);
        // 保存历程表
        FloodIncidentRecord floodIncidentRecord = generateRecord(saveVO, floodIncidentEsu, operate);

        // 更新主表，历史记录主键
        saveVO.setRecordId(floodIncidentRecord.getId());
        return floodIncidentDao.updateById(saveVO);
    }


    @Override
    public int deleteById(Serializable id) {
        FloodIncident floodIncident = floodIncidentDao.selectById(id);
        if(floodIncident!=null){
            floodIncidentRecordDao.deleteById(floodIncident.getRecordId());
        }
        return super.deleteById(id);
    }

    /**
     * 生成操作讲述表
     *
     * @param oldVO
     * @param floodIncidentEsu
     * @param operate          申请 下发 审核 驳回  接受 拒绝 到达  完成
     * @return
     */
    private FloodIncidentRecord generateRecord(FloodIncident oldVO, FloodIncidentEsu floodIncidentEsu, String operate) {
        FloodIncidentRecord floodIncidentRecordDto = new FloodIncidentRecord();
        floodIncidentRecordDto.setIncidentId(oldVO.getId());
        floodIncidentRecordDto.setPreviousStatus(oldVO.getStatus());

        // 新状态
        floodIncidentRecordDto.setNewStatus(floodIncidentEsu.getStatus());

        // 操作
        floodIncidentRecordDto.setOperate(operate);

        // 操作人相关信息
        floodIncidentRecordDto.setCorpId(floodIncidentEsu.getOperateUserCorpId());
        floodIncidentRecordDto.setCorpName(floodIncidentEsu.getOperateUserCorpName());
        floodIncidentRecordDto.setUserId(floodIncidentEsu.getOperateUserId());
        floodIncidentRecordDto.setUserName(floodIncidentEsu.getOperateUserName());

        // 驳回原因
        floodIncidentRecordDto.setRemarks(floodIncidentEsu.getReason());
        floodIncidentRecordDao.insert(floodIncidentRecordDto);
        return floodIncidentRecordDto;
    }

    @Override
    public void auditOrReject(FloodIncidentEsu esu) {
        if (esu.getTag() == null) {
            throw new BusinessException("审核标识不能为空");
        }
        esu.setStatus(esu.getTag());
        esu.setReviewTime(new Date());
        super.update(esu);

        FloodIncident oldVO = floodIncidentDao.selectById(esu.getId());
        generateRecord(oldVO, esu, esu.getTag() == 0 ? "驳回" : "审核");
    }

    @Override
    public void acceptOrRefuse(FloodIncidentEsu esu) {
        if (esu.getTag() == 0) {
            esu.setStatus(4);
        } else if (esu.getTag() == 1) {
            esu.setStatus(2);
            esu.setSubmissionTime(new Date());
        } else {
            throw new BusinessException("接收标识非法");
        }
        FloodIncident oldVO = floodIncidentDao.selectById(esu.getId());
        super.update(esu);

        generateRecord(oldVO, esu, esu.getTag() == 0 ? "拒绝" : "接收");
    }

    @Override
    public void updateRescueTime(FloodIncidentEsu esu) {
        esu.setActualArrivalTime(new Date());
        FloodIncident oldVO = floodIncidentDao.selectById(esu.getId());
        esu.setStatus(oldVO.getStatus());
        super.update(esu);
        generateRecord(oldVO, esu, "完成");
    }

    @Override
    public void finishRescueTime(FloodIncidentEsu esu) {
        esu.setStatus(3);
        esu.setFloodMitigationCompletionTime(new Date());
        super.update(esu);
        FloodIncident oldVO = floodIncidentDao.selectById(esu.getId());
        generateRecord(oldVO, esu, "完成");
    }
}
