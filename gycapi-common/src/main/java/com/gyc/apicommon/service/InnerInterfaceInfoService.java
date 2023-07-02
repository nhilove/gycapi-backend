package com.gyc.apicommon.service;

import com.gyc.apicommon.model.entity.InterfaceInfo;

/**
 * @author ASUS
 * @description 针对表【interface_info(接口信息表)】的数据库操作Service
 * @createDate 2023-06-24 10:22:04
 */
public interface InnerInterfaceInfoService {

    /**
     * 从数据库中查询模拟接口是否存在（请求路径、请求方法、请求参数，返回接口信息，为空表示不存在）
     *
     * @param path
     * @param method
     * @return
     */
    InterfaceInfo getInvokerInterfaceInfo(String path, String method);


}
