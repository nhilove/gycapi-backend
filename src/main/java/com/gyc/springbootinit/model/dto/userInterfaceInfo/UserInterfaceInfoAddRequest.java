package com.gyc.springbootinit.model.dto.userInterfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @author gyc
 */
@Data
public class UserInterfaceInfoAddRequest implements Serializable {

    /**
     * 调用者 id
     */
    private Long userId;

    /**
     * 接口 id
     */
    private Long interfaceInfoId;

    /**
     * 接口总调用次数
     */
    private Integer totalNum;

    /**
     * 接口剩余调用次数
     */
    private Integer leftNum;




    private static final long serialVersionUID = 1L;
}