package com.java.yintao.order.service.impl;

import com.java.yintao.order.base.appoint.AppointDbService;
import com.java.yintao.order.base.order.OrderDbService;
import com.java.yintao.order.domain.entity.AppointEntity;
import com.java.yintao.order.domain.entity.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yintao
 * @desc 写清楚这个类的作用
 * @package com.java.yintao.order.service.impl
 * @date 2021/3/2823:04
 */
@Component
public class OrderManagerService {


    @Autowired
    OrderDbService orderDbService;

    @Autowired
    AppointDbService appointDbService;

    @Transactional(rollbackFor = Exception.class,readOnly = true)
    public Boolean saveOrderAndAppoint(OrderEntity orderEntity, List<AppointEntity> appointEntityList){
        orderDbService.insert(orderEntity);
        for (int i = 0; i < appointEntityList.size(); i++) {
            appointDbService.insert(appointEntityList.get(i));
        }
        return Boolean.TRUE;
    }


}
