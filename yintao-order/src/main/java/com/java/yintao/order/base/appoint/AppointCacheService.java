package com.java.yintao.order.base.appoint;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yintao.order.config.RedisUtil;
import com.java.yintao.order.domain.entity.AppointEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yintao
 * @desc 写清楚这个类的作用
 * @package com.java.yintao.order.service.cache
 * @date 2021/3/2810:20
 */
@Component
public class AppointCacheService {

    private static final String APPOINT_KEY= "MY_APPOINT:";

    private static final String APPOINT_PAGE_PREFIX = "APPOINT_PAGE_";

    private static final Integer TIMEOUT = 5;

    @Value("${mysql.to.es.flag}")
    private Boolean esFlag;

    @Autowired
    AppointEsService appointEsService;

    @Autowired
    RedisUtil redisUtil;

    public void saveAppointCache(AppointEntity appointEntity, Integer result){
        if(result > 0) {
            redisUtil.set(buildAppointKey(appointEntity), appointEntity,TIMEOUT, TimeUnit.DAYS);
        }
        //获取开关配置，是否写ES
        if(esFlag){
            System.out.println("数据同步写入es");
            appointEsService.saveAppointToEs(appointEntity);
            //发送补偿落库mq
            System.out.println("发送补偿MQ");
        }
    }

    public void delOrderCache(AppointEntity appointEntity,Integer result){
        if(result > 0) {
            redisUtil.del(buildAppointKey(appointEntity));
        }
        if(esFlag){
            appointEsService.updateAppointToEs(appointEntity);
            //发送补偿mq
            System.out.println("发送补偿MQ");
        }
    }

    public AppointEntity getAppointCache(AppointEntity appointEntity){
        Object value = redisUtil.get(buildAppointKey(appointEntity));
        if(null != value){
            System.out.println("缓存查询单条数据");
            return JSON.parseObject(value.toString(), AppointEntity.class);
        }
        //获取开关配置，查询Es
        if(esFlag){
            //查询ES
            System.out.println("单条信息降级查询ES");
            return appointEsService.queryAppointInfoFromEs(appointEntity);
        }
        return null;
    }

    public List<AppointEntity> queryAppointPageCache(Page<AppointEntity> page, String userPin){
        //查询缓存
        Object value = redisUtil.get(buildPageKey(page, userPin));
        if(null != value){
            System.out.println("分页缓存数据返回");
            return (List<AppointEntity>)value;
        }
        //查询ES
        return appointEsService.queryAppointPageFromEs(page,buildAppointEntity(userPin));
    }

    private AppointEntity buildAppointEntity(String userPin) {
        AppointEntity orderEntity = new AppointEntity();
        orderEntity.setUserPin(userPin);
        return orderEntity;
    }

    public void saveAppointPageCache(Page<AppointEntity> page, String userPin){
        System.out.println("保存分页数据缓存");
        redisUtil.set(buildPageKey(page,userPin),page.getRecords(),TIMEOUT, TimeUnit.SECONDS);
    }

    private String buildPageKey(Page<AppointEntity> page, String userPin) {
        return String.format("%s%s%s%s",APPOINT_PAGE_PREFIX,userPin,page.getCurrent(),page.getSize());
    }

    private String buildAppointKey(AppointEntity appointEntity) {
        return String.format("%s%s",APPOINT_KEY,appointEntity.getJdAppointmentId());
    }


}
