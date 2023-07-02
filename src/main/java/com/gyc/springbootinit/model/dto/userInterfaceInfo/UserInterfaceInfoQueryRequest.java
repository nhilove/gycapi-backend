package com.gyc.springbootinit.model.dto.userInterfaceInfo;

import com.gyc.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author gyc
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInterfaceInfoQueryRequest extends PageRequest implements Serializable {

    /**
     * 主键id
     */
    private Long id;

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

    /**
     * 接口状态 0正常（默认）1禁用
     */
    private Integer status;


    private static final long serialVersionUID = 1L;
}