package com.gyc.springbootinit.model.vo;

import com.google.gson.Gson;
import com.gyc.springbootinit.model.entity.InterfaceInfo;
import com.gyc.springbootinit.model.entity.UserInterfaceInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

;

/**
 * 帖子视图
 *
 * @author gyc
 */
@Data
public class UserInterfaceInfoVO implements Serializable {

    private final static Gson GSON = new Gson();

    /**
     * 主键id
     */
    private Long id;

    /**
     * 调用人id
     */
    private Long userId;

    /**
     * 接口id
     */
    private Long userInterfaceInfoId;


    /**
     * 接口总调用次数
     */
    private Integer totalNum;

    /**
     * 接口剩余调用次数
     */
    private Integer leftNum;


    /**
     * 接口状态 0关闭（默认）1开启
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    /**
     * 创建人信息
     */
    private UserVO user;


    /**
     * 包装类转对象
     *
     * @param userInterfaceInfoVO
     * @return
     */
    public static InterfaceInfo voToObj(UserInterfaceInfoVO userInterfaceInfoVO) {
        if (userInterfaceInfoVO == null) {
            return null;
        }
        InterfaceInfo userInterfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoVO, userInterfaceInfo);
        return userInterfaceInfo;
    }

    /**
     * 对象转包装类
     *
     * @param userInterfaceInfo
     * @return
     */
    public static UserInterfaceInfoVO objToVo(UserInterfaceInfo userInterfaceInfo) {
        if (userInterfaceInfo == null) {
            return null;
        }
        UserInterfaceInfoVO userInterfaceInfoVO = new UserInterfaceInfoVO();
        BeanUtils.copyProperties(userInterfaceInfo, userInterfaceInfoVO);

        return userInterfaceInfoVO;
    }
}
