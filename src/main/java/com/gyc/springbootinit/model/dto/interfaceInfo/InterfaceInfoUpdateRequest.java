package com.gyc.springbootinit.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 *
 * @author gyc
 */
@Data
public class InterfaceInfoUpdateRequest implements Serializable {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 创建人
     */
    private Long userId;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 描述
     */
    private String description;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 接口状态 0关闭（默认）1开启
     */
    private Integer status;


    private static final long serialVersionUID = 1L;
}