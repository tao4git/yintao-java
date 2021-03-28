package com.java.yintao.order.base.appoint;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.yintao.order.domain.entity.AppointEntity;
import com.java.yintao.order.mapper.AppointMapper;
import org.springframework.stereotype.Service;

/**
 * @author yintao
 * @desc 写清楚这个类的作用
 * @package com.java.yintao.order.base
 * @date 2021/3/27 21:56
 */
@Service
public class AppointDbService extends ServiceImpl<AppointMapper, AppointEntity>{

    public Integer insert(AppointEntity appointEntity) {
        return baseMapper.insert(appointEntity);
    }

    public Integer updateAppointStatus(AppointEntity appointEntity) {
        UpdateWrapper<AppointEntity> updateWrapper = new UpdateWrapper<>();
        LambdaQueryWrapper<AppointEntity> wrapper = new QueryWrapper<AppointEntity>().lambda();
        wrapper.eq(AppointEntity::getUserPin, appointEntity.getUserPin());
        wrapper.eq(AppointEntity::getJdAppointmentId,appointEntity.getJdAppointmentId());
        return baseMapper.update(appointEntity, updateWrapper);
    }


    public AppointEntity queryInfoByAppointId(String userPin,Long jdAppointmentId) {
        QueryWrapper<AppointEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_pin",userPin);
        queryWrapper.eq("jd_appointment_id",jdAppointmentId);
        AppointEntity orderEntity = baseMapper.selectOne(queryWrapper);
        if(null == orderEntity){
            //缓存空对象，防止击穿
            orderEntity = new AppointEntity();
        }
        return orderEntity;
    }

    public AppointEntity queryInfoByUserId(String userPin,Long userId,Long companyNo) {
        QueryWrapper<AppointEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_pin",userPin);
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq("company_no",companyNo);
        AppointEntity orderEntity = baseMapper.selectOne(queryWrapper);
        if(null == orderEntity){
            //缓存空对象，防止击穿
            orderEntity = new AppointEntity();
        }
        return orderEntity;
    }

    public Page<AppointEntity> queryAppointPageByPin(Page<AppointEntity> page, String userPin) {
        if(null == page){
            page = new Page<>();
        }
        QueryWrapper<AppointEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_pin",userPin);
        queryWrapper.orderByDesc("create_time");
        Page<AppointEntity> orderEntityPage = baseMapper.selectPage(page, queryWrapper);
        return orderEntityPage;
    }
}
