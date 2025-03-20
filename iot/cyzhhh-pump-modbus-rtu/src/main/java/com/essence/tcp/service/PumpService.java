package com.essence.tcp.service;

public interface PumpService {
    /**
     * 将读取的数据入库
     * @param source
     */
    public void doExplainAndSave(String source);

    void pumpOpenOrClose(String deviceAddr,Integer pNum, Integer status) throws InterruptedException;
}
