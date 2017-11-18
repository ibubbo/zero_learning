package net.imain.common;

import net.imain.pojo.User;
import net.imain.vo.UserInfoVo;
import org.springframework.beans.BeanUtils;

/**
 * 全局转换器
 *
 * @author: uncle
 * @apdateTime: 2017-11-18 14:51
 */
public class HandlerConverter {

    /**
     * userInfoVo 转 user
     *
     * @param userInfoVo 数据
     * @return
     */
    public static User userInfoToUser(UserInfoVo userInfoVo) {
        User user = new User();
        BeanUtils.copyProperties(userInfoVo, user);
        return user;
    }
}
