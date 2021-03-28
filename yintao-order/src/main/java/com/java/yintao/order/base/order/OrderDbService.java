package com.java.yintao.order.base.order;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.yintao.order.domain.entity.OrderEntity;
import com.java.yintao.order.mapper.OrderMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author yintao
 * @desc 写清楚这个类的作用
 * @package com.java.yintao.order.base
 * @date 2021/3/27 21:56
 */
@Service
public class OrderDbService extends ServiceImpl<OrderMapper, OrderEntity>{

    public Integer insert(OrderEntity orderEntity) {
        return baseMapper.insert(orderEntity);
    }

    public Integer updateOrderStatus(OrderEntity orderEntity) {
        UpdateWrapper<OrderEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_pin", orderEntity.getUserPin());
        updateWrapper.eq("order_id", orderEntity.getOrderId());
        updateWrapper.set("pay_status", 1);
        updateWrapper.set("pay_time", new Date());
        return baseMapper.update(orderEntity, updateWrapper);
    }


    public OrderEntity queryOrderInfoByPin(String userPin,Long orderId) {
        QueryWrapper<OrderEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_pin",userPin);
        queryWrapper.eq("order_id",orderId);
        OrderEntity orderEntity = baseMapper.selectOne(queryWrapper);
        if(null == orderEntity){
            //缓存空对象，防止击穿
            orderEntity = new OrderEntity();
        }
        return orderEntity;
    }

    public Page<OrderEntity> queryOrderPageByPin(Page<OrderEntity> page, String userPin) {
        if(null == page){
            page = new Page<>();
        }
        QueryWrapper<OrderEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_pin",userPin);
        queryWrapper.orderByDesc("create_time");
        Page<OrderEntity> orderEntityPage = baseMapper.selectPage(page, queryWrapper);
        return orderEntityPage;
    }
}
