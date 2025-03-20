package com.essence.service.impl;

import com.essence.common.constant.ItemConstant;
import com.essence.dao.XjRyxxDao;
import com.essence.dao.XjbhDao;
import com.essence.dao.XjrwDao;
import com.essence.dao.XjrwlcDao;
import com.essence.dao.entity.XjRyxxDto;
import com.essence.dao.entity.XjbhDto;
import com.essence.dao.entity.XjrwDto;
import com.essence.dao.entity.XjrwlcDto;
import com.essence.interfaces.api.XjrwService;
import com.essence.interfaces.api.XjrwlcService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.XjrwEsr;
import com.essence.interfaces.model.XjrwEsu;
import com.essence.interfaces.model.XjrwlcEsr;
import com.essence.interfaces.param.XjrwEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterXjrwEtoT;
import com.essence.service.converter.ConverterXjrwTtoR;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 设备巡检任务(Xjrw)业务层
 *
 * @author majunjie
 * @since 2025-01-09 15:09:04
 */
@Service
public class XjrwServiceImpl extends BaseApiImpl<XjrwEsu, XjrwEsp, XjrwEsr, XjrwDto> implements XjrwService {
    @Autowired
    private XjrwDao xjrwDao;
    @Autowired
    private ConverterXjrwEtoT converterXjrwEtoT;
    @Autowired
    private ConverterXjrwTtoR converterXjrwTtoR;
    @Autowired
    private XjrwlcService xjrwlcService;
    @Autowired
    private XjRyxxDao xjRyxxDao;
    @Autowired
    private XjrwlcDao xjrwlcDao;
    @Autowired
    private XjbhDao xjbhDao;

    public XjrwServiceImpl(XjrwDao xjrwDao, ConverterXjrwEtoT converterXjrwEtoT, ConverterXjrwTtoR converterXjrwTtoR) {
        super(xjrwDao, converterXjrwEtoT, converterXjrwTtoR);
    }

    @Override
    public Paginator<XjrwlcEsr> searchXjlc(PaginatorParam param) {
        return xjrwlcService.findByPaginator(param);
    }

    private synchronized Integer bm(String type) {
        Integer bm = 1;
        switch (type) {
            case "0":
                XjbhDto xjbhDto = xjbhDao.selectById(type);
                bm=xjbhDto.getBh()+1;
                xjbhDto.setBh(bm);
                xjbhDao.updateById(xjbhDto);
                break;
            case "1":
                XjbhDto xjbhDto1 = xjbhDao.selectById(type);
                bm=xjbhDto1.getBh()+1;
                xjbhDto1.setBh(bm);
                xjbhDao.updateById(xjbhDto1);
                break;
            case "2":
                XjbhDto xjbhDto2 = xjbhDao.selectById(type);
                bm=xjbhDto2.getBh()+1;
                xjbhDto2.setBh(bm);
                xjbhDao.updateById(xjbhDto2);
                break;
            default:
                break;
        }
        return bm;
    }

    @Override
    public XjrwEsr addXjrw(XjrwEsu xjrwEsu, String userName) {
        XjrwDto xjrwDto = converterXjrwEtoT.toBean(xjrwEsu);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = new Date();
        String dayStr = sdf.format(time).substring(0, 10);
        String dayStrS = dayStr.replace("-", "");
        XjRyxxDto xjRyxxDto = xjRyxxDao.selectById(xjrwEsu.getFzrId());

        if (xjrwEsu.getType().equals("0")) {

            //摄像头
            //1.添加巡检任务
            xjrwDto.setBh(dayStrS + "SBXJ" + String.format("%04d", bm(xjrwEsu.getType())));
            xjrwDto.setJssj(DateUtils.addDays(time, 7));
        } else {
            //会议室
            xjrwDto.setBh(dayStrS + "HYSXJ" + String.format("%04d",  bm(xjrwEsu.getType())));
            xjrwDto.setJssj(DateUtils.addDays(xjrwDto.getJhsj(), 1));
        }
        xjrwDto.setId(UUID.randomUUID().toString().replace("-", ""));
        xjrwDto.setGdmc(dayStrS + xjrwEsu.getMc() + "设备巡检工单");
        xjrwDto.setLy("1");
        xjrwDto.setLx("0");
        if (null != xjRyxxDto) {
            xjrwDto.setLxfs(xjRyxxDto.getLxfs());
        }
        xjrwDto.setJhsj(time);
        xjrwDto.setZt(0);
        xjrwDto.setWcqk(0);
        xjrwDto.setFxwt(0);
        xjrwDto.setJjsx(0);
        xjrwDto.setCjsj(time);
        xjrwDto.setPfry(userName);
        xjrwDao.insert(xjrwDto);
        //2.添加巡检任务历程
        XjrwlcDto xjrwlcDto = new XjrwlcDto();
        xjrwlcDto.setId(UUID.randomUUID().toString().replace("-", ""));
        xjrwlcDto.setRwId(xjrwDto.getId());
        xjrwlcDto.setMs(ItemConstant.XJSCGD);
        xjrwlcDto.setCzr(xjrwDto.getPfry());
        xjrwlcDto.setCjsj(time);
        xjrwlcDao.insert(xjrwlcDto);
        return converterXjrwTtoR.toBean(xjrwDto);
    }

    public XjrwDto addXjWtrw(XjrwDto xjrwDto1) {
        //1.添加巡检任务
        XjrwDto xjrwDto = new XjrwDto();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = new Date();
        String dayStr = sdf.format(time).substring(0, 10);
        String dayStrS = dayStr.replace("-", "");
        xjrwDto.setId(UUID.randomUUID().toString().replace("-", ""));
        xjrwDto.setGdmc(dayStrS + xjrwDto1.getMc() + "设备巡检问题工单");
        xjrwDto.setBh(dayStrS + "SBXJWT" + String.format("%04d",  bm("2")));
        xjrwDto.setLy("2");
        xjrwDto.setMc(xjrwDto1.getMc());

        xjrwDto.setType(xjrwDto1.getType());
        xjrwDto.setLx("1");
        xjrwDto.setRiverName(xjrwDto1.getRiverName());
        xjrwDto.setAddress(xjrwDto1.getAddress());
        xjrwDto.setLgtd(xjrwDto1.getLgtd());

        xjrwDto.setLttd(xjrwDto1.getLttd());
        xjrwDto.setXjnr(xjrwDto1.getXjnr());
        xjrwDto.setFzr(null);
        xjrwDto.setFzrId(null);
        xjrwDto.setLxfs(null);

        xjrwDto.setJhsj(time);
        xjrwDto.setSjsj(null);
        xjrwDto.setJssj(null);
        xjrwDto.setZt(6);
        xjrwDto.setWcqk(0);

        xjrwDto.setFxwt(0);
        xjrwDto.setYj(null);
        xjrwDto.setCjsj(time);
        xjrwDto.setDkjd(null);
        xjrwDto.setDkwd(null);

        xjrwDto.setDkdz(null);
        xjrwDto.setDkjltp(null);
        xjrwDto.setWtms(xjrwDto1.getWtms());
        xjrwDto.setDktp(xjrwDto1.getDktp());
        xjrwDto.setCltp(xjrwDto1.getCltp());

        xjrwDto.setClcs(xjrwDto1.getClcs());
        xjrwDto.setFxsj(time);
        xjrwDto.setPfsj(null);
        xjrwDto.setJjsj(null);
        xjrwDto.setBzmc(xjrwDto1.getBzmc());

        xjrwDto.setPfry(null);
        xjrwDto.setBzId(xjrwDto1.getBzId());
        xjrwDto.setSbr(xjrwDto1.getFzr());
        xjrwDto.setParentId(xjrwDto1.getId());
        xjrwDto.setZzly(null);
        xjrwDto.setJjsx(0);
        xjrwDao.insert(xjrwDto);
        XjrwlcDto xjrwlcDto = new XjrwlcDto();
        xjrwlcDto.setId(UUID.randomUUID().toString().replace("-", ""));
        xjrwlcDto.setRwId(xjrwDto.getId());
        xjrwlcDto.setMs(ItemConstant.XJSCGD);
        xjrwlcDto.setCzr(xjrwDto.getPfry());
        xjrwlcDto.setCjsj(time);
        xjrwlcDao.insert(xjrwlcDto);
        return xjrwDto;
    }

    @Override
    public XjrwEsr updateXjrw(XjrwEsu xjrwEsu, String userName) {
        XjrwDto xjrwDto = xjrwDao.selectById(xjrwEsu.getId());
        XjrwDto xjrwDto1 = converterXjrwEtoT.toBean(xjrwEsu);

        if (!String.valueOf(xjrwDto.getZt()).equals(String.valueOf(xjrwDto1.getZt()))) {
            XjrwlcDto xjrwlcDto = new XjrwlcDto();
            xjrwlcDto.setId(UUID.randomUUID().toString().replace("-", ""));
            xjrwlcDto.setRwId(xjrwDto.getId());
            xjrwlcDto.setCjsj(new Date());
            //修改完成情况
            switch (String.valueOf(xjrwDto1.getZt())) {
                case "0":
                    xjrwDto1.setWcqk(0);
                    xjrwlcDto.setMs(ItemConstant.XJPFGD);
                    xjrwlcDto.setCzr(userName);
                    break;
                case "1":
                    xjrwDto1.setWcqk(1);
                    if (null!=xjrwDto1.getJssj()&&(xjrwDto1.getJssj().getTime() < new Date().getTime())) {
                        xjrwDto1.setWcqk(2);
                    }
                    xjrwlcDto.setMs(ItemConstant.XJZZGD);
                    xjrwlcDto.setCzr(userName);
                    break;
                case "2":
                    xjrwDto1.setWcqk(0);
                    //填充实际巡检时间
                    xjrwDto1.setSjsj(new Date());
                    xjrwlcDto.setMs(ItemConstant.XJKQGD);
                    xjrwlcDto.setCzr(userName);
                    break;
                case "3":
                    xjrwDto1.setWcqk(0);
                    xjrwDto1.setFxsj(new Date());
                    xjrwDto1.setJjsj(new Date());
                    xjrwlcDto.setMs(ItemConstant.XJDSHGD);
                    xjrwlcDto.setCzr(userName);

                    break;
                case "4":
                    xjrwDto1.setWcqk(0);
                    xjrwlcDto.setMs(ItemConstant.XJSHGD);
                    xjrwlcDto.setCzr(userName);
                    break;
                case "5":
                    xjrwDto1.setWcqk(1);
                    if (xjrwDto1.getJssj().getTime() < new Date().getTime()) {
                        xjrwDto1.setWcqk(2);
                    }
                    if (xjrwDto1.getFxwt() > 0) {
                        //生成新的问题工单
                        addXjWtrw(xjrwDto1);
                    }
                    xjrwlcDto.setMs(ItemConstant.XJWCGD);
                    xjrwlcDto.setCzr(userName);
                    break;
                default:
                    break;
            }
            xjrwlcDao.insert(xjrwlcDto);
        }
        if ((StringUtils.isNotBlank(xjrwDto1.getFzr())&&StringUtils.isNotBlank(xjrwDto.getFzr()))&&(!xjrwDto1.getFzr().equals(xjrwDto.getFzr()))) {
            XjrwlcDto xjrwlcDto = new XjrwlcDto();
            xjrwlcDto.setId(UUID.randomUUID().toString().replace("-", ""));
            xjrwlcDto.setRwId(xjrwDto.getId());
            xjrwlcDto.setCjsj(new Date());
            xjrwlcDto.setMs(ItemConstant.XJFZRBGGD);
            xjrwlcDto.setCzr(userName);
            xjrwlcDao.insert(xjrwlcDto);
        }
        if (null!=xjrwDto1.getZt()&&(xjrwDto1.getZt()<6&&xjrwDto1.getZt()>4)){
            xjrwDto1.setWcqk(1);
            if (xjrwDto1.getJssj().getTime() < new Date().getTime()) {
                xjrwDto1.setWcqk(2);
            }
        }
        xjrwDao.updateById(xjrwDto1);
        return converterXjrwTtoR.toBean(xjrwDto1);
    }
}
