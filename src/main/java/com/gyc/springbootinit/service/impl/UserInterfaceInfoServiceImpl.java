package com.gyc.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.gyc.springbootinit.common.ErrorCode;
import com.gyc.springbootinit.constant.CommonConstant;
import com.gyc.springbootinit.exception.BusinessException;
import com.gyc.springbootinit.mapper.UserInterfaceInfoMapper;
import com.gyc.springbootinit.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.gyc.springbootinit.model.entity.User;
import com.gyc.springbootinit.model.entity.UserInterfaceInfo;
import com.gyc.springbootinit.model.vo.UserInterfaceInfoVO;
import com.gyc.springbootinit.model.vo.UserVO;
import com.gyc.springbootinit.service.UserInterfaceInfoService;
import com.gyc.springbootinit.service.UserService;
import com.gyc.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ASUS
 * @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service实现
 * @createDate 2023-06-29 10:14:12
 */
@Slf4j
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
        implements UserInterfaceInfoService {

    private final static Gson GSON = new Gson();

    @Resource
    private UserService userService;

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        Long id = userInterfaceInfo.getId();
        Long userId = userInterfaceInfo.getUserId();
        Long interfaceInfoId = userInterfaceInfo.getInterfaceInfoId();
        Integer totalNum = userInterfaceInfo.getTotalNum();
        Integer leftNum = userInterfaceInfo.getLeftNum();
        Integer status = userInterfaceInfo.getStatus();


        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 首次创建时，参数不能为空
        if (add) {
            if (userId < 0 || interfaceInfoId < 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        // 有参数则校验
        if (leftNum < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余次数不能小于0");
        }
        if (totalNum < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "总次数不能小于0");
        }

    }

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        if (interfaceInfoId < 0 || userId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interfaceInfoId", interfaceInfoId);
        updateWrapper.eq("userId", userId);
        updateWrapper.setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");
        return this.update(updateWrapper);
    }


    /**
     * 获取查询包装类
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {

        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        if (userInterfaceInfoQueryRequest == null) {
            return queryWrapper;
        }

        String sortField = userInterfaceInfoQueryRequest.getSortField();
        String sortOrder = userInterfaceInfoQueryRequest.getSortOrder();
        Long id = userInterfaceInfoQueryRequest.getId();
        Long userId = userInterfaceInfoQueryRequest.getUserId();
        Long interfaceInfoId = userInterfaceInfoQueryRequest.getInterfaceInfoId();
        Integer status = userInterfaceInfoQueryRequest.getStatus();
        Integer totalNum = userInterfaceInfoQueryRequest.getTotalNum();
        Integer leftNum = userInterfaceInfoQueryRequest.getLeftNum();

        //queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(interfaceInfoId), "interfaceInfoId", interfaceInfoId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public Page<UserInterfaceInfo> searchFromEs(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        return null;
    }

    @Override
    public UserInterfaceInfoVO getUserInterfaceInfoVO(UserInterfaceInfo userInterfaceInfo, HttpServletRequest request) {
        UserInterfaceInfoVO userInterfaceInfoVO = UserInterfaceInfoVO.objToVo(userInterfaceInfo);
        long userInterfaceInfoId = userInterfaceInfo.getId();
        // 1. 关联查询用户信息
        Long userId = userInterfaceInfo.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        userInterfaceInfoVO.setUser(userVO);
        // 2. 已登录，获取用户点赞、收藏状态
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {

        }
        return userInterfaceInfoVO;
    }

    @Override
    public Page<UserInterfaceInfoVO> getUserInterfaceInfoVOPage(Page<UserInterfaceInfo> userInterfaceInfoPage, HttpServletRequest request) {
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoPage.getRecords();
        Page<UserInterfaceInfoVO> userInterfaceInfoVOPage = new Page<>(userInterfaceInfoPage.getCurrent(), userInterfaceInfoPage.getSize(), userInterfaceInfoPage.getTotal());
        if (CollectionUtils.isEmpty(userInterfaceInfoList)) {
            return userInterfaceInfoVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = userInterfaceInfoList.stream().map(UserInterfaceInfo::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));

        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            Set<Long> userInterfaceInfoIdSet = userInterfaceInfoList.stream().map(UserInterfaceInfo::getId).collect(Collectors.toSet());
            loginUser = userService.getLoginUser(request);

        }
        // 填充信息
        List<UserInterfaceInfoVO> userInterfaceInfoVOList = userInterfaceInfoList.stream().map(userInterfaceInfo -> {
            UserInterfaceInfoVO userInterfaceInfoVO = UserInterfaceInfoVO.objToVo(userInterfaceInfo);
            Long userId = userInterfaceInfo.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            userInterfaceInfoVO.setUser(userService.getUserVO(user));
            return userInterfaceInfoVO;
        }).collect(Collectors.toList());
        userInterfaceInfoVOPage.setRecords(userInterfaceInfoVOList);
        return userInterfaceInfoVOPage;
    }

}




