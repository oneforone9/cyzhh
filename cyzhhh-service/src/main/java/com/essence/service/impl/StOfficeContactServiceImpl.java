package com.essence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.utils.PageUtil;
import com.essence.common.utils.ResponseResult;
import com.essence.dao.StOfficeContactDao;
import com.essence.dao.entity.StGreenReportDto;
import com.essence.dao.entity.StOfficeContactDto;
import com.essence.interfaces.api.StOfficeBaseRelationService;
import com.essence.interfaces.api.StOfficeBaseService;
import com.essence.interfaces.api.StOfficeContactService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.StOfficeContactEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStOfficeContactEtoT;
import com.essence.service.converter.ConverterStOfficeContactTtoR;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * (StOfficeContact)业务层
 * @author liwy
 * @since 2023-03-29 18:49:40
 */
@Service
public class StOfficeContactServiceImpl extends BaseApiImpl<StOfficeContactEsu, StOfficeContactEsp, StOfficeContactEsr, StOfficeContactDto> implements StOfficeContactService {

    @Autowired
    private StOfficeContactDao stOfficeContactDao;
    @Autowired
    private ConverterStOfficeContactEtoT converterStOfficeContactEtoT;
    @Autowired
    private ConverterStOfficeContactTtoR converterStOfficeContactTtoR;
    @Autowired
    private StOfficeContactService stOfficeContactService;
    @Autowired
    private StOfficeBaseRelationService stOfficeBaseRelationService;
    @Autowired
    private StOfficeBaseService stOfficeBaseService;

    public StOfficeContactServiceImpl(StOfficeContactDao stOfficeContactDao, ConverterStOfficeContactEtoT converterStOfficeContactEtoT, ConverterStOfficeContactTtoR converterStOfficeContactTtoR) {
        super(stOfficeContactDao, converterStOfficeContactEtoT, converterStOfficeContactTtoR);
    }

    /**
     * 获取当前登录人的常用联系人
     * @param stOfficeContactEsuParam
     * @param param
     * @return
     */
    @Override
    public Paginator<StOfficeContactEsr> searchByUserId(StOfficeContactEsuParam stOfficeContactEsuParam, PaginatorParam param) {
        PaginatorParam paginatorParam = stOfficeContactEsuParam.getPaginatorParam();
        int pageSize = paginatorParam.getPageSize();
        int currentPage = paginatorParam.getCurrentPage();
        String deptName = stOfficeContactEsuParam.getDeptName(); //科室名
        String userName = stOfficeContactEsuParam.getUserName(); //使用人姓名
        String officeTelephone = stOfficeContactEsuParam.getOfficeTelephone(); //办公电话
        String phoneNumber = stOfficeContactEsuParam.getPhoneNumber(); //移动电话
        String roomNo = stOfficeContactEsuParam.getRoomNo();//房间号

        //先查询所有的
        List<StOfficeContactEsr> resList = null;
        Paginator<StOfficeContactEsr> paginator = stOfficeContactService.findByPaginator(param);
        List<StOfficeContactEsr> items = paginator.getItems();
        for (int i = 0; i < items.size(); i++) {
            StOfficeContactEsr stOfficeContactEsr = items.get(i);
            //根据联系人id获取联系人相关信息s
            StOfficeBaseRelationEsr stOfficeBaseRelationEsr = stOfficeBaseRelationService.findById(stOfficeContactEsr.getOfficeBaseRelationId());
            if(stOfficeBaseRelationEsr !=null){
                stOfficeContactEsr.setOfficeBaseId(stOfficeBaseRelationEsr.getOfficeBaseId());
                stOfficeContactEsr.setUserName(stOfficeBaseRelationEsr.getUserName());
                stOfficeContactEsr.setOfficeTelephone(stOfficeBaseRelationEsr.getOfficeTelephone());
                stOfficeContactEsr.setJob(stOfficeBaseRelationEsr.getJob());
                stOfficeContactEsr.setPhoneNumber(stOfficeBaseRelationEsr.getPhoneNumber());
                stOfficeContactEsr.setRoomNo(stOfficeBaseRelationEsr.getRoomNo());
            }
            //根据科室id获取到科室信息
            StOfficeBaseEsr stOfficeBaseEsr = stOfficeBaseService.findById(stOfficeBaseRelationEsr.getOfficeBaseId());
            if(stOfficeBaseEsr !=null){
                stOfficeContactEsr.setDeptName(stOfficeBaseEsr.getDeptName());
            }
        }
        resList = items.stream().collect(Collectors.toList());
        if (!"".equals(deptName) && deptName != null) {
            resList = resList.stream().filter(p -> p.getDeptName().contains(deptName)).collect(Collectors.toList());
        }
        if (!"".equals(userName) && userName != null) {
            resList = resList.stream().filter(p -> p.getUserName().contains(userName)).collect(Collectors.toList());
        }
        if(!"".equals(officeTelephone) && officeTelephone != null){
            resList = resList.stream().filter(p -> p.getOfficeTelephone().contains(officeTelephone)).collect(Collectors.toList());
        }
        if(!"".equals(phoneNumber) && phoneNumber != null){
            resList = resList.stream().filter(p -> p.getPhoneNumber().contains(phoneNumber)).collect(Collectors.toList());
        }
        if(!"".equals(roomNo) && roomNo != null){
            resList = resList.stream().filter(p -> p.getRoomNo().contains(roomNo)).collect(Collectors.toList());
        }

        //进行手动分页
        PageUtil<StOfficeContactEsr> pageUtil = new PageUtil(resList, currentPage, pageSize, null, null);
        List<StOfficeContactEsr> recordsList = pageUtil.getRecords();

        //返回分页
        Paginator<StOfficeContactEsr> resPaginator = new Paginator<>();
        resPaginator.setPageSize(pageUtil.getPageSize());
        resPaginator.setCurrentPage(pageUtil.getCurrent());
        resPaginator.setTotalCount(pageUtil.getTotal());
        resPaginator.setPageCount(pageUtil.getPages());
        resPaginator.setItems(recordsList);

        return resPaginator;
    }

    /**
     * 收藏常用联系人
     * @param stOfficeContactEsu
     * @return
     */
    @Override
    public ResponseResult addStOfficeContact(StOfficeContactEsu stOfficeContactEsu) {
        int insert;
        //先判断当前登录人是否已经收藏此联系人
        QueryWrapper queryWrapper = new  QueryWrapper();
        queryWrapper.eq("user_id",stOfficeContactEsu.getUserId());
        queryWrapper.eq("office_base_relation_id",stOfficeContactEsu.getOfficeBaseRelationId());
        List list = stOfficeContactDao.selectList(queryWrapper);
        //已收藏则不需要再次收藏
        if(list.size()!=0){
            return ResponseResult.success("已收藏,不需要再次收藏",null);

        }else{
            insert= stOfficeContactService.insert(stOfficeContactEsu);//未收藏则进行收藏
            return ResponseResult.success("收藏常用联系人成功",insert);
        }
    }

    /**
     * 取消收藏常用联系人
     * @param id
     * @return
     */
    @Override
    public Object deleteStOfficeContact(Integer id) {


        int i = stOfficeContactService.deleteById(id);
        return null;
    }


}
