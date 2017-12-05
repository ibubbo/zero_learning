package net.imain.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import net.imain.common.HandlerCheck;
import net.imain.common.HandlerResult;
import net.imain.dao.ShippingMapper;
import net.imain.enums.HandlerEnum;
import net.imain.enums.ShippingEnum;
import net.imain.pojo.Shipping;
import net.imain.service.ShippingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author: uncle
 * @apdateTime: 2017-12-03 10:22
 */
@Service
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public HandlerResult saveOrUpdate(Integer userId, Shipping shipping) {
        Map map = Maps.newHashMap();
        // 数据校验
        shipping.setUserId(userId);
        if (HandlerCheck.ObjectIsEmpty(shipping)) {
            return HandlerResult.error(HandlerEnum.ILLEGAL_ARGUMENT.getCode(),
                    HandlerEnum.ILLEGAL_ARGUMENT.getMessage());
        }

        Shipping resultShipping = shippingMapper.selectByPrimaryKey(shipping.getId());
        if (HandlerCheck.ObjectIsNotEmpty(resultShipping)) {
            // 更新，注意需要防止横向越权
            int updateShippingSum = shippingMapper.updateByShipping(shipping);
            if (updateShippingSum == 0) {
                return HandlerResult.error(ShippingEnum.SHIPPING_UPDATE_ERROR.getMessage());
            }
            return HandlerResult.success(HandlerEnum.SUCCESS.getMessage());
        }
        // 新增
        int insertShippingSum = shippingMapper.insert(shipping);
        if (insertShippingSum == 0) {
            return HandlerResult.error(ShippingEnum.SHIPPING_INSERT_ERROR.getMessage());
        }
        map.put("shippingId", shipping.getId());
        return HandlerResult.success(map);
    }

    @Override
    public HandlerResult<String> del(Integer userId, Integer shippingId) {
        if (HandlerCheck.NumIsEmpty(shippingId)) {
            return HandlerResult.error(HandlerEnum.ILLEGAL_ARGUMENT.getCode(),
                    HandlerEnum.ILLEGAL_ARGUMENT.getMessage());
        }
        // 防止横向越权
        int deleteShippingSum = shippingMapper.deleteByShippingIdAndUserId(userId, shippingId);
        if (deleteShippingSum == 0) {
            return HandlerResult.error(ShippingEnum.SHIPPING_DELETE_ERROR.getMessage());
        }
        return HandlerResult.success(HandlerEnum.SUCCESS.getMessage());
    }

    @Override
    public HandlerResult<Shipping> select(Integer userId, Integer shippingId) {

        if (HandlerCheck.NumIsEmpty(shippingId)) {
            return HandlerResult.error(HandlerEnum.ILLEGAL_ARGUMENT.getCode(),
                    HandlerEnum.ILLEGAL_ARGUMENT.getMessage());
        }

        Shipping shipping = shippingMapper.selectByShippingIdAndUserId(userId, shippingId);
        if (HandlerCheck.ObjectIsEmpty(shipping)) {
            return HandlerResult.success(StringUtils.EMPTY);
        }
        return HandlerResult.success(shipping);
    }

    @Override
    public HandlerResult<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        if (HandlerCheck.ObjectIsEmpty(shippingList)) {
            return HandlerResult.success(StringUtils.EMPTY);
        }
        PageInfo pageInfo = new PageInfo(shippingList);
        return HandlerResult.success(pageInfo);
    }
}
