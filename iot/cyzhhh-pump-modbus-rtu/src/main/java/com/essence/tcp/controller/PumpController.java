package com.essence.tcp.controller;

import com.essence.tcp.service.PumpService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("pump")
public class PumpController {
    @Resource
    private PumpService pumpService;

    /**
     * 开关泵站
     * @param deviceAddr 站点地址
     * @param status 开关状态 1 开 0 关闭
     * @param pNum 第几个泵 p1 p2
     *
     * @return
     */
    @PostMapping("/status")
    public Integer pumpOpenOrClose(String deviceAddr,Integer pNum,Integer status) throws InterruptedException {
        pumpService.pumpOpenOrClose(deviceAddr,pNum,status);
        return 0;
    }
}
