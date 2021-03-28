package com.java.yintao.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yintao.order.domain.entity.AppointEntity;

import java.util.List;

/**
 * @author yintao
 * @desc 写清楚这个类的作用
 * @package com.java.yintao.order.service
 * @date 2021/3/2122:23
 */
public interface AppointBaseService {

    Integer insert(AppointEntity appointEntity);

    Integer insertAppointList(List<AppointEntity> appointEntityList);

    Integer updateAppointStatus(AppointEntity appointEntity);

    AppointEntity queryAppointInfoByPin(String userPin, Long jdAppointmentId);

    Page<AppointEntity> queryAppointPageByPin(Page<AppointEntity> page, String userPin);
}
