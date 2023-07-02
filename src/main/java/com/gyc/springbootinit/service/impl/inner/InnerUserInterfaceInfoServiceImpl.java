package com.gyc.springbootinit.service.impl.inner;

import com.gyc.apicommon.service.InnerUserInterfaceInfoService;
import com.gyc.springbootinit.service.impl.UserInterfaceInfoServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 远程调用用户接口关系实现
 * @author gyc
 */
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {
    @Resource
    private UserInterfaceInfoServiceImpl userInterfaceInfoService;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {

        return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }
}
