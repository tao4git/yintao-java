package com.java.yintao.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yintao.order.domain.entity.OrderEntity;

/**
 * @author yintao
 * @desc 写清楚这个类的作用
 * @package com.java.yintao.order.service
 * @date 2021/3/2122:23
 */
public interface OrderBaseService {

    Integer insert(OrderEntity orderEntity);

    Integer updateOrderStatus(OrderEntity orderEntity);

    OrderEntity queryOrderInfoByPin(String userPin,Long orderId);

    Page<OrderEntity> queryOrderPageByPin(Page<OrderEntity> page,String userPin);
}
