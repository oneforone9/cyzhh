package com.essence.web.portal;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.essence.common.dto.StStbprpBEntityDTO;
import com.essence.common.utils.HttpUtils;
import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.StSideGateService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.StSideGateEsp;
import com.essence.interfaces.vaild.Update;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 边闸基础表管理
 * @author BINX
 * @since 2023-01-17 11:05:19
 */
@RestController
@RequestMapping("/stSideGate")
public class StSideGateController extends BaseController<Integer, StSideGateEsu, StSideGateEsp, StSideGateEsr> {
    @Autowired
    private StSideGateService stSideGateService;

    public StSideGateController(StSideGateService stSideGateService) {
        super(stSideGateService);
    }
    //获取卫星云图
    @Value("${control.url}")
    private String controlUrl;

    /**
     * 查询场站列表条件
     * @param stStbprpBEntityDTO 条件
     * @return
     */
    @PostMapping("info/list")
    public ResponseResult<List<StStbprpBEntityDTO>> getStatInfoList(@RequestBody StStbprpBEntityDTO stStbprpBEntityDTO){
        ResponseResult stationInfoList = stSideGateService.getStationInfoList(stStbprpBEntityDTO);
        return stationInfoList;
    }

    /**
     * 更新
     *
     * @param e
     * @return
     */
    @PostMapping("/x/update")
    @ResponseBody
    public ResponseResult update(@RequestBody StStbprpBEntityDTO e) {
        stSideGateService.updateData(e);
        return ResponseResult.success("更新成功", null);
    }


    /**
     * 查询闸坝负责人信息
     * @param stSideGateEsuParam
     * @return
     */
    @PostMapping("getStSideGateRelation")
    public ResponseResult<Paginator<StSideGateEsrRes>> getStSideGateRelation(@RequestBody StSideGateEsuParam stSideGateEsuParam){
        Paginator<StSideGateEsrRes> p = stSideGateService.getStSideGateRelation(stSideGateEsuParam);
        return ResponseResult.success("查询成功", p);
    }

    /**
     * 泵站远程控制指令
     * @param deviceAddr 站点地址
     * @param status 开关状态 1 开 0 关闭
     * @param pNum 第几个泵 p1 p2
     * @return
     */
    @GetMapping("controlCommand")
    public ResponseResult controlCommand(@RequestParam String deviceAddr,@RequestParam  Integer pNum,@RequestParam Integer status) throws Exception {
        JSONObject paramJson = new JSONObject();
        paramJson.put("deviceAddr",deviceAddr);
        paramJson.put("pNum",pNum);
        paramJson.put("status",status);
        //通过deviceAddr 去查询 泵站表中的 stcd 去关联
        //并且去查看几标段 solt 1-6
        stSideGateService.dealRemoteSoltPunp(deviceAddr,pNum,status, paramJson);

        return ResponseResult.success("远程控制指令成功", null);
    }


    /**
     * 统计水闸泵站的总数、远控、正常以及故障的数量
     * @param sttp
     * @return
     */
    @GetMapping("getStSideGate")
    public ResponseResult getStSideGate(@RequestParam String sttp){
        Map<String,Object> map = stSideGateService.getStSideGate(sttp);
        return ResponseResult.success("查询成功", map);
    }

    /**
     * 查询泵站
     */
    @GetMapping("/getPump")
    public ResponseResult<List<StSideGateEsr>> getPump(String caseId){
        return ResponseResult.success("查询成功", stSideGateService.getPump(caseId));
    }

    /**
     * 闸坝实时工况
     * @param sttp
     * @return
     */
    @GetMapping("getStSideGateNow")
    public ResponseResult getStSideGateNow(@RequestParam String sttp, String stnm, String rvnm, String p1Hitch, String p2Hitch, String m00,String m01,String m02){
        List list = stSideGateService.getStSideGateNow(sttp, stnm, rvnm, p1Hitch, p2Hitch, m00, m01,m02);
        return ResponseResult.success("查询成功", list);
    }
}
