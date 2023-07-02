package com.gyc.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gyc.springbootinit.model.entity.UserInterfaceInfo;

import java.util.List;


/**
* @author ASUS
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Mapper
* @createDate 2023-06-29 10:14:12
* @Entity com.gyc.springbootinit.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {
    //返回前limit条接口的调用情况
    List<UserInterfaceInfo> invokeTopInterfaceInfo(int limit);

}




