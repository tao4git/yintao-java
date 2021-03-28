package com.java.yintao.order.base.appoint;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yintao.order.domain.entity.AppointEntity;
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
public class AppointEsService {

    public static HashMap<String, HashMap<Long, AppointEntity>> appointEsMap = new HashMap<>();

    public void saveAppointToEs(AppointEntity appointEntity){
        HashMap<Long, AppointEntity> appointHashMap = appointEsMap.get(appointEntity.getUserPin());
        if(CollectionUtil.isEmpty(appointHashMap)){
            appointHashMap = new HashMap<>();
        }
        appointHashMap.put(appointEntity.getJdAppointmentId(),appointEntity);
        appointEsMap.put(appointEntity.getUserPin(),appointHashMap);
    }

    public void updateAppointToEs(AppointEntity appointEntity){
        AppointEntity orderEntity1 = queryAppointInfoFromEs(appointEntity);
        if(null == orderEntity1){
            return;
        }
        BeanUtil.copyProperties(appointEntity,orderEntity1);
        saveAppointToEs(orderEntity1);
    }

    public List<AppointEntity> queryAppointPageFromEs(Page<AppointEntity> page, AppointEntity orderEntity){
        HashMap<Long, AppointEntity> longOrderEntityHashMap = appointEsMap.get(orderEntity.getUserPin());
        if(CollectionUtil.isEmpty(longOrderEntityHashMap)){
            return new ArrayList<>();
        }
        return ListUtil.toList(longOrderEntityHashMap.values());
    }

    public AppointEntity queryAppointInfoFromEs(AppointEntity appointEntity){
        HashMap<Long, AppointEntity> longOrderEntityHashMap = appointEsMap.get(appointEntity.getUserPin());
        return longOrderEntityHashMap.get(appointEntity.getJdAppointmentId());
    }
}
