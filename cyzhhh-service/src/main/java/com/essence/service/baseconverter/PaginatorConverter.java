package com.essence.service.baseconverter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.essence.interfaces.entity.Paginator;

import java.util.List;

/**
 * @author zhy
 * @since 2022/5/11 17:20
 */

public class PaginatorConverter<T, R> {
    public static <T, R> Paginator<R> IPageToPaginator(IPage<T> iPage, List<R> list) {
        Paginator<R> tPaginator = new Paginator();
        tPaginator.setItems(list);

        tPaginator.setCurrentPage(((Long) iPage.getCurrent()).intValue());

        tPaginator.setPageCount(((Long) iPage.getPages()).intValue());

        tPaginator.setPageSize(((Long) iPage.getSize()).intValue());

        tPaginator.setTotalCount(((Long) iPage.getTotal()).intValue());

        return tPaginator;
    }

    public static <T, R> Paginator<R> pageToPaginator(Paginator<T> iPage, List<R> list) {
        Paginator<R> tPaginator = new Paginator();
        tPaginator.setItems(list);

        tPaginator.setCurrentPage(iPage.getCurrentPage());

        tPaginator.setPageCount( iPage.getPageCount());

        tPaginator.setPageSize(iPage.getPageSize());

        tPaginator.setTotalCount( iPage.getTotalCount());

        return tPaginator;
    }
}
