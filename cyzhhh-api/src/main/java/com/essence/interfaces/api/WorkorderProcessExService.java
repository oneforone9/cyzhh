package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.WorkorderProcessExEsr;
import com.essence.interfaces.model.WorkorderProcessExEsu;
import com.essence.interfaces.param.WorkorderProcessExEsp;

/**
 * 工单处理过程表服务层
 *
 * @author zhy
 * @since 2022-10-27 15:26:31
 */
public interface WorkorderProcessExService extends BaseApi<WorkorderProcessExEsu, WorkorderProcessExEsp, WorkorderProcessExEsr> {

}
