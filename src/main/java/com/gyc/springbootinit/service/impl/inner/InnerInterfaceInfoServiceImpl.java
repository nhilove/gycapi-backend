package com.gyc.springbootinit.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gyc.apicommon.model.entity.InterfaceInfo;
import com.gyc.apicommon.service.InnerInterfaceInfoService;
import com.gyc.springbootinit.common.ErrorCode;
import com.gyc.springbootinit.exception.BusinessException;
import com.gyc.springbootinit.mapper.InterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 远程调用interfeace服务实现
 * @author gyc
 */
@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public InterfaceInfo getInvokerInterfaceInfo(String path, String method) {

        if (StringUtils.isAnyBlank(path, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<com.gyc.springbootinit.model.entity.InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", path);
        queryWrapper.eq("method", method);
        com.gyc.springbootinit.model.entity.InterfaceInfo interfaceInfo = interfaceInfoMapper.selectOne(queryWrapper);
        InterfaceInfo interfaceInfo1 = new InterfaceInfo();
        interfaceInfo1.setId(interfaceInfo.getId());
        interfaceInfo1.setName(interfaceInfo.getName());
        interfaceInfo1.setUserId(interfaceInfo.getUserId());
        interfaceInfo1.setUrl(interfaceInfo.getUrl());
        interfaceInfo1.setMethod(interfaceInfo.getMethod());
        interfaceInfo1.setDescription(interfaceInfo.getDescription());
        interfaceInfo1.setRequestParams(interfaceInfo.getRequestParams());
        interfaceInfo1.setRequestHeader(interfaceInfo.getRequestHeader());
        interfaceInfo1.setResponseHeader(interfaceInfo.getResponseHeader());
        return interfaceInfo1;
    }
}
