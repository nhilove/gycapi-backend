package com.gyc.apicommon.service;

/**
 * @author ASUS
 * @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service
 * @createDate 2023-06-29 10:14:12
 */
public interface InnerUserInterfaceInfoService {

    /**
     * 用户调用接口统计
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);



}
