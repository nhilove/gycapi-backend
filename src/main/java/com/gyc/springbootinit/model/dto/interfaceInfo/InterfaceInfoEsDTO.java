package com.gyc.springbootinit.model.dto.interfaceInfo;

import com.google.gson.Gson;
import com.gyc.springbootinit.model.entity.InterfaceInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * 帖子 ES 包装类
 *
 * @author gyc
 **/
// todo 取消注释开启 ES（须先配置 ES）
//@Document(indexName = "interfaceInfo")
@Data
public class InterfaceInfoEsDTO implements Serializable {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * id
     */
    @Id
    private Long id;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = DATE_TIME_PATTERN)
    private Date createTime;

    /**
     * 更新时间
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = DATE_TIME_PATTERN)
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    private static final long serialVersionUID = 1L;

    private static final Gson GSON = new Gson();

    /**
     * 对象转包装类
     *
     * @param interfaceInfo
     * @return
     */
    public static InterfaceInfoEsDTO objToDto(InterfaceInfo interfaceInfo) {
        if (interfaceInfo == null) {
            return null;
        }
        InterfaceInfoEsDTO interfaceInfoEsDTO = new InterfaceInfoEsDTO();
        BeanUtils.copyProperties(interfaceInfo, interfaceInfoEsDTO);

        return interfaceInfoEsDTO;
    }

    /**
     * 包装类转对象
     *
     * @param interfaceInfoEsDTO
     * @return
     */
    public static InterfaceInfo dtoToObj(InterfaceInfoEsDTO interfaceInfoEsDTO) {
        if (interfaceInfoEsDTO == null) {
            return null;
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoEsDTO, interfaceInfo);

        return interfaceInfo;
    }
}
