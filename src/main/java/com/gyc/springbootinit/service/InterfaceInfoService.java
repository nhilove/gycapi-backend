package com.gyc.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gyc.springbootinit.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.gyc.springbootinit.model.entity.InterfaceInfo;
import com.gyc.springbootinit.model.vo.InterfaceInfoVO;


import javax.servlet.http.HttpServletRequest;

/**
 * @author ASUS
 * @description 针对表【interface_info(接口信息表)】的数据库操作Service
 * @createDate 2023-06-24 10:22:04
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {



    /**
     * 校验
     *
     * @param interfaceInfo
     * @param add
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

    QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

    Page<InterfaceInfo> searchFromEs(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

    InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo, HttpServletRequest request);

    Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage, HttpServletRequest request);


}
