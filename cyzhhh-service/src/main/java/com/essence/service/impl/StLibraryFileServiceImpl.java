package com.essence.service.impl;

import com.essence.dao.StLibraryFileDao;
import com.essence.dao.entity.StLibraryFileDto;
import com.essence.interfaces.api.StLibraryFileService;
import com.essence.interfaces.model.StLibraryFileEsr;
import com.essence.interfaces.model.StLibraryFileEsu;
import com.essence.interfaces.param.StLibraryFileEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStLibraryFileEtoT;
import com.essence.service.converter.ConverterStLibraryFileTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * 知识库文件表(StLibraryFile)业务层
 * @author liwy
 * @since 2023-08-17 10:21:16
 */
@Service
public class StLibraryFileServiceImpl extends BaseApiImpl<StLibraryFileEsu, StLibraryFileEsp, StLibraryFileEsr, StLibraryFileDto> implements StLibraryFileService {

    @Autowired
    private StLibraryFileDao stLibraryFileDao;
    @Autowired
    private ConverterStLibraryFileEtoT converterStLibraryFileEtoT;
    @Autowired
    private ConverterStLibraryFileTtoR converterStLibraryFileTtoR;

    public StLibraryFileServiceImpl(StLibraryFileDao stLibraryFileDao, ConverterStLibraryFileEtoT converterStLibraryFileEtoT, ConverterStLibraryFileTtoR converterStLibraryFileTtoR) {
        super(stLibraryFileDao, converterStLibraryFileEtoT, converterStLibraryFileTtoR);
    }
}
