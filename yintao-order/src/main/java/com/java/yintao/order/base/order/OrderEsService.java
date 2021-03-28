package com.java.yintao.order.base.order;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yintao.order.domain.entity.OrderEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author yintao
 * @desc 写清楚这个类的作用
 * @package com.java.yintao.order.service.cache
 * @date 2021/3/2812:07
 */
@Component
public class OrderEsService {

    public static HashMap<String, HashMap<Long,OrderEntity>> orderEsMap = new HashMap<>();

    public void saveOrderToEs(OrderEntity orderEntity){
        HashMap<Long, OrderEntity> longOrderEntityHashMap = orderEsMap.get(orderEntity.getUserPin());
        if(CollectionUtil.isEmpty(longOrderEntityHashMap)){
            longOrderEntityHashMap = new HashMap<>();
        }
        longOrderEntityHashMap.put(orderEntity.getOrderId(),orderEntity);
        orderEsMap.put(orderEntity.getUserPin(),longOrderEntityHashMap);
    }

    public void updateOrderToEs(OrderEntity orderEntity){
        OrderEntity orderEntity1 = queryOrderInfoFromEs(orderEntity);
        if(null == orderEntity1){
            return;
        }
        BeanUtil.copyProperties(orderEntity,orderEntity1);
        saveOrderToEs(orderEntity1);
    }

    public List<OrderEntity> queryOrderPageFromEs(Page<OrderEntity> page, OrderEntity orderEntity){
        HashMap<Long, OrderEntity> longOrderEntityHashMap = orderEsMap.get(orderEntity.getUserPin());
        if(CollectionUtil.isEmpty(longOrderEntityHashMap)){
            return new ArrayList<>();
        }
        return ListUtil.toList(longOrderEntityHashMap.values());
    }

    public OrderEntity queryOrderInfoFromEs(OrderEntity orderEntity){
        HashMap<Long, OrderEntity> longOrderEntityHashMap = orderEsMap.get(orderEntity.getUserPin());
        return longOrderEntityHashMap.get(orderEntity.getOrderId());
    }
}
