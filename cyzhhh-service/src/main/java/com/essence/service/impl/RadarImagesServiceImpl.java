package com.essence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.RadarImagesLdDao;
import com.essence.dao.RadarImagesLddzDao;
import com.essence.dao.RadarImagesWxDao;
import com.essence.dao.entity.RadarImagesLdDto;
import com.essence.dao.entity.RadarImagesLddzDto;
import com.essence.dao.entity.RadarImagesWxDto;
import com.essence.interfaces.api.RadarImagesLdService;
import com.essence.interfaces.api.RadarImagesLddzService;
import com.essence.interfaces.api.RadarImagesService;
import com.essence.interfaces.api.RadarImagesWxService;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.RadarImagesLdEsr;
import com.essence.interfaces.model.RadarImagesLddzEsr;
import com.essence.interfaces.model.RadarImagesWxEsr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 卫星雷达图片实业务实现
 * @Author BINX
 * @Description TODO
 * @Date 2023/4/23 13:40
 */
@Service
public class RadarImagesServiceImpl implements RadarImagesService {

    @Autowired
    RadarImagesLdService radarImagesLdService;
    @Autowired
    RadarImagesLddzService radarImagesLddzService;
    @Autowired
    RadarImagesWxService radarImagesWxService;

    @Autowired
    RadarImagesLdDao radarImagesLdDao;
    @Autowired
    RadarImagesLddzDao radarImagesLddzDao;
    @Autowired
    RadarImagesWxDao radarImagesWxDao;

    @Override
    public Object getRadarLdImage(PaginatorParam param) {

        PaginatorParam param1 = new PaginatorParam();
        ArrayList<Criterion> orders = new ArrayList<>();
        Criterion dateDesc = new Criterion();
        dateDesc.setFieldName("imageDate");
        dateDesc.setOperator(Criterion.ASC);
        orders.add(dateDesc);
        param1.setOrders(orders);
        Paginator<RadarImagesLdEsr> byPaginator = radarImagesLdService.findByPaginator(param1);
        List<RadarImagesLdEsr> items = byPaginator.getItems();
        int size = items.size();
        List<RadarImagesLdEsr> last12 = new ArrayList<>();
        if(size >= param.getPageSize()) {
            last12 = items.subList(size - param.getPageSize(), size);  // 取最后n条数据
            byPaginator.setItems(last12);
        }
        return byPaginator;
    }

    @Override
    public Object getRadarLddzImage(PaginatorParam param) {
        PaginatorParam param1 = new PaginatorParam();
        ArrayList<Criterion> orders = new ArrayList<>();
        Criterion dateDesc = new Criterion();
        dateDesc.setFieldName("imageDate");
        dateDesc.setOperator(Criterion.ASC);
        orders.add(dateDesc);
        param1.setOrders(orders);
        Paginator<RadarImagesLddzEsr> byPaginator = radarImagesLddzService.findByPaginator(param1);
        List<RadarImagesLddzEsr> items = byPaginator.getItems();
        int size = items.size();
        List<RadarImagesLddzEsr> last12 = new ArrayList<>();
        if(size >= param.getPageSize()) {
            last12 = items.subList(size - param.getPageSize(), size);  // 取最后n条数据
            byPaginator.setItems(last12);
        }
        return byPaginator;
    }

    @Override
    public Object getRadarWxImage(PaginatorParam param) {
        PaginatorParam param1 = new PaginatorParam();
        ArrayList<Criterion> orders = new ArrayList<>();
        Criterion dateDesc = new Criterion();
        dateDesc.setFieldName("imageDate");
        dateDesc.setOperator(Criterion.ASC);
        orders.add(dateDesc);
        param1.setOrders(orders);
        Paginator<RadarImagesWxEsr> byPaginator = radarImagesWxService.findByPaginator(param1);
        List<RadarImagesWxEsr> items = byPaginator.getItems();
        int size = items.size();
        List<RadarImagesWxEsr> last12 = new ArrayList<>();
        if(size >= param.getPageSize()) {
            last12 = items.subList(size - param.getPageSize(), size);  // 取最后n条数据
            byPaginator.setItems(last12);
        }
        return byPaginator;
    }
}
