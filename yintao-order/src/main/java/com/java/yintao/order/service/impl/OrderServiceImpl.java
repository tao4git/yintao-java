package com.java.yintao.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.yintao.order.domain.entity.OrderEntity;
import com.java.yintao.order.mapper.OrderMapper;
import com.java.yintao.order.service.OrderService;
import org.springframework.stereotype.Service;

/**
 * @author yintao
 * @desc 写清楚这个类的作用
 * @package com.java.yintao.order.service.impl
 * @date 2021/3/2122:24
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper,OrderEntity> implements OrderService  {


    @Override
    public int insert(OrderEntity orderEntity) {
        return baseMapper.insert(orderEntity);
    }
}
