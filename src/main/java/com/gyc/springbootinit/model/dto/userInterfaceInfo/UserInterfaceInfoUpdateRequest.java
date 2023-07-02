package com.gyc.springbootinit.model.dto.userInterfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 *
 * @author gyc
 */
@Data
public class UserInterfaceInfoUpdateRequest implements Serializable {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 接口总调用次数
     */
    private Integer totalNum;

    /**
     * 接口剩余调用次数
     */
    private Integer leftNum;

    /**
     * 接口状态 0正常（默认）1禁用
     */
    private Integer status;




    private static final long serialVersionUID = 1L;
}