package com.java.yintao.order.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yintao.order.base.appoint.AppointCacheService;
import com.java.yintao.order.base.appoint.AppointDbService;
import com.java.yintao.order.base.appoint.AppointEsService;
import com.java.yintao.order.domain.entity.AppointEntity;
import com.java.yintao.order.service.AppointBaseService;
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
public class AppointBaseServiceImpl implements AppointBaseService {

    @Autowired
    AppointDbService appointDbService;

    @Autowired
    AppointCacheService appointCacheService;

    @Autowired
    AppointEsService appointEsService;

    @Value("${mysql.to.es.flag}")
    private Boolean esFlag;

    @Override
    public Integer insert(AppointEntity orderEntity) {
        int result = 0;
        try {
//            int i = 1/0;
            result = appointDbService.insert(orderEntity);
        }catch (Exception e) {
            if(esFlag){
                result = 1;
            }
            log.error("OrderServiceImpl insert order excepiton....");
        }
        appointCacheService.saveAppointCache(orderEntity,result);
        return result;
    }

    @Override
    public Integer updateAppointStatus(AppointEntity orderEntity) {
        int result = 0;
        try {
//            int i = 1/0;
            result = appointDbService.updateAppointStatus(orderEntity);
        }catch (Exception e){
            if(esFlag){
                result = 1;
            }
            log.error("OrderServiceImpl updateOrderStatus order excepiton....");
        }
        appointCacheService.delOrderCache(orderEntity,result);
        return result;
    }


    @Override
    public AppointEntity queryAppointInfoByPin(String userPin,Long orderId) {
        AppointEntity appointCache = appointCacheService.getAppointCache(buildAppointEntity(userPin, orderId));
        if(null != appointCache){
            return appointCache;
        }
        AppointEntity orderEntity = appointDbService.queryInfoByAppointId(userPin,orderId);
        appointCacheService.saveAppointCache(orderEntity,1);
        return orderEntity;
    }

    private AppointEntity buildAppointEntity(String userPin, Long orderId) {
        AppointEntity orderEntity = new AppointEntity();
        orderEntity.setUserPin(userPin);
        orderEntity.setJdAppointmentId(orderId);
        return orderEntity;
    }

    @Override
    public Page<AppointEntity> queryAppointPageByPin(Page<AppointEntity> page,String userPin) {
        if(null == page){
            page = new Page<>();
        }
        List<AppointEntity> orderEntities = appointCacheService.queryAppointPageCache(page, userPin);
        //TODO es降级开关，直接查库,否则ES返回空，会继续走库查询
        if(CollectionUtils.isEmpty(orderEntities)){
            page.setRecords(new ArrayList<>());
            return page;
        }
        Page<AppointEntity> orderEntityPage = appointDbService.queryAppointPageByPin(page, userPin);
        appointCacheService.saveAppointPageCache(orderEntityPage,userPin);
        return orderEntityPage;
    }
}
