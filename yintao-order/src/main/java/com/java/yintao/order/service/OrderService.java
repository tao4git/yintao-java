package com.java.yintao.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yintao.order.domain.entity.OrderEntity;

/**
 * @author yintao
 * @desc 写清楚这个类的作用
 * @package com.java.yintao.order.service
 * @date 2021/3/2122:23
 */
public interface OrderService {

    int insert(OrderEntity orderEntity);

    Page<OrderEntity> queryOrderPage(Page<OrderEntity> page,OrderEntity orderEntity);
}
