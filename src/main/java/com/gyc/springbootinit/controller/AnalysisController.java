package com.gyc.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gyc.springbootinit.annotation.AuthCheck;
import com.gyc.springbootinit.common.BaseResponse;
import com.gyc.springbootinit.common.ErrorCode;
import com.gyc.springbootinit.common.ResultUtils;
import com.gyc.springbootinit.constant.UserConstant;
import com.gyc.springbootinit.exception.BusinessException;
import com.gyc.springbootinit.mapper.UserInterfaceInfoMapper;
import com.gyc.springbootinit.model.entity.InterfaceInfo;
import com.gyc.springbootinit.model.entity.UserInterfaceInfo;
import com.gyc.springbootinit.model.vo.InterfaceInfoVO;
import com.gyc.springbootinit.service.InterfaceInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分析接口
 * @author gyc
 */
@RestController
@RequestMapping( "/analysis" )
public class AnalysisController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @GetMapping( "/top/list/invoke" )
    @AuthCheck( mustRole = UserConstant.ADMIN_ROLE )
    public BaseResponse<List<InterfaceInfoVO>> invokeTopInterfaceInfo() {

        List<UserInterfaceInfo> userInterfaceInfos = userInterfaceInfoMapper.invokeTopInterfaceInfo(3);
        Map<Long, List<UserInterfaceInfo>> interfaceInfoIdObjMap = userInterfaceInfos.stream().collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", interfaceInfoIdObjMap.keySet());
        List<InterfaceInfo> list = interfaceInfoService.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        List<InterfaceInfoVO> collect = list.stream().map(interfaceInfo -> {
            InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
            Integer totalNum = interfaceInfoIdObjMap.get(interfaceInfo.getId()).get(0).getTotalNum();
            interfaceInfoVO.setTotalNum(totalNum);
            return interfaceInfoVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(collect);
    }

}
