package com.gyc.apicommon.service;

import com.gyc.apicommon.model.entity.User;

/**
 * 用户服务
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public interface InnerUserService {


    /**
     * 数据库中查是否已分配给用户秘钥(accessKey、secretKey，返回用户信息，为空表示不存在)
     *
     * @param accessKey
     * @param
     * @return
     */
    User getInvokerUser(String accessKey);


}
