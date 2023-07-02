package com.gyc.springbootinit.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gyc.apicommon.model.entity.User;
import com.gyc.apicommon.service.InnerUserService;
import com.gyc.springbootinit.common.ErrorCode;
import com.gyc.springbootinit.exception.BusinessException;
import com.gyc.springbootinit.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 远程调用用户实现
 * @author gyc
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {


    @Resource
    private UserMapper userMapper;

    @Override
    public User getInvokerUser(String accessKey) {
        if (StringUtils.isAnyBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<com.gyc.springbootinit.model.entity.User> query = new QueryWrapper<>();
        query.eq("accessKey", accessKey);
        com.gyc.springbootinit.model.entity.User user = userMapper.selectOne(query);
        User user1 = new User();
        user1.setId(user.getId());
        user1.setUserAccount(user.getUserAccount());
        user1.setUserPassword(user.getUserPassword());
        user1.setAccessKey(user.getAccessKey());
        user1.setSecretKey(user.getSecretKey());
        user1.setUnionId(user.getUnionId());
        user1.setMpOpenId(user.getMpOpenId());
        user1.setUserName(user.getUserName());
        user1.setUserAvatar(user.getUserAvatar());
        user1.setUserProfile(user.getUserProfile());
        user1.setUserRole(user.getUserRole());
        return user1;

    }
}
