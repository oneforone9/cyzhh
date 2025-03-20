package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.HtglEsp;
import org.springframework.web.multipart.MultipartFile;

/**
 * 合同管理服务层
 * @author majunjie
 * @since 2024-09-09 17:45:36
 */
public interface HtglService extends BaseApi<HtglEsu,HtglEsp,HtglEsr> {
    HtglEsr addHt(HtglEsuData htglEsuData);

    String deleteHt(HtDel htDel);

    String importHt(MultipartFile file,String userId );

    Paginator<HtglfjEsr> htfjSearch(PaginatorParam param);

    Paginator<HtglysxmEsr> htysxmSearch(PaginatorParam param);

    Paginator<HtgllcEsr> htlxSearch(PaginatorParam param);

    HtglEsr addHtScd(HtglScd htglScd);

    HtglEsr updateHqdKz(HtglEsuDatas htglEsuDatas);

    HtglEsr updateHqdks(HtglEsuDatas htglEsuDatas);

    HtglEsr addHtHsz(Hthsz hthsz);

    HtglEsr addHtGq(Hthsz hthsz);

    HtglEsr addHtSc(Hthsz hthsz);

    String importHtFj(HtglFjImports htglFjImports);

    HtglEsr addHtByTz(HtglEsu htglEsu);
}
