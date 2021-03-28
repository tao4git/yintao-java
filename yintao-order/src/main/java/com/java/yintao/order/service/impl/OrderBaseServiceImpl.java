package com.java.yintao.order.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yintao.order.base.order.OrderCacheService;
import com.java.yintao.order.base.order.OrderDbService;
import com.java.yintao.order.base.order.OrderEsService;
import com.java.yintao.order.domain.entity.OrderEntity;
import com.java.yintao.order.service.OrderBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yintao
 * @desc 写清楚这个类的作用
 * @package com.java.yintao.order.service.impl
 * @date 2021/3/2122:24
 */
@Service
@Slf4j
public class OrderBaseServiceImpl implements OrderBaseService {

    @Autowired
    OrderDbService orderDbService;

    @Autowired
    OrderCacheService orderCacheService;

    @Autowired
    OrderEsService orderEsService;

    @Value("${mysql.to.es.flag}")
    private Boolean esFlag;

    @Override
    public Integer insert(OrderEntity orderEntity) {
        int result = 0;
        try {
//            int i = 1/0;
            result = orderDbService.insert(orderEntity);
        }catch (Exception e) {
            if(esFlag){
                result = 1;
            }
            log.error("OrderServiceImpl insert order excepiton....",e);
        }
        orderCacheService.saveOrderCache(orderEntity,result);
        return result;
    }

    @Override
    public Integer updateOrderStatus(OrderEntity orderEntity) {
        int result = 0;
        try {
//            int i = 1/0;
            result = orderDbService.updateOrderStatus(orderEntity);
        }catch (Exception e){
            if(esFlag){
                result = 1;
            }
            log.error("OrderServiceImpl updateOrderStatus order excepiton....");
        }
        orderCacheService.delOrderCache(orderEntity,result);
        return result;
    }


    @Override
    public OrderEntity queryOrderInfoByPin(String userPin,Long orderId) {
        OrderEntity orderCache = orderCacheService.getOrderCache(buildOrderEntity(userPin, orderId));
        if(null != orderCache){
            return orderCache;
        }
        OrderEntity orderEntity = orderDbService.queryOrderInfoByPin(userPin,orderId);
        orderCacheService.saveOrderCache(orderEntity,1);
        return orderEntity;
    }

    private OrderEntity buildOrderEntity(String userPin, Long orderId) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUserPin(userPin);
        orderEntity.setOrderId(orderId);
        return orderEntity;
    }

    @Override
    public Page<OrderEntity> queryOrderPageByPin(Page<OrderEntity> page,String userPin) {
        if(null == page){
            page = new Page<>();
        }
        List<OrderEntity> orderEntities = orderCacheService.queryOrderPageCache(page, userPin);
        //TODO es降级开关，直接查库,否则ES返回空，会继续走库查询
        if(CollectionUtils.isEmpty(orderEntities)){
            page.setRecords(new ArrayList<>());
            return page;
        }
        Page<OrderEntity> orderEntityPage = orderDbService.queryOrderPageByPin(page, userPin);
        orderCacheService.saveOrderPageCache(orderEntityPage,userPin);
        return orderEntityPage;
    }
}
