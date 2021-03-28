package com.java.yintao.order.base.order;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yintao.order.config.RedisUtil;
import com.java.yintao.order.domain.entity.OrderEntity;
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
public class OrderCacheService {

    private static final String ORDER_KEY= "MY_ORDER:";

    private static final String PAGE_PREFIX = "ORDER_PAGE_";

    private static final Integer TIMEOUT = 5;

    @Value("${mysql.to.es.flag}")
    private Boolean esFlag;

    @Autowired
    OrderEsService orderEsService;

    @Autowired
    RedisUtil redisUtil;

    public void saveOrderCache(OrderEntity orderEntity,Integer result){
        if(result > 0) {
            redisUtil.set(buildOrderKey(orderEntity), orderEntity,TIMEOUT, TimeUnit.DAYS);
        }
        //获取开关配置，是否写ES
        if(esFlag){
            System.out.println("数据同步写入es");
            orderEsService.saveOrderToEs(orderEntity);
            //发送补偿落库mq
            System.out.println("发送补偿MQ");
        }
    }

    public void saveOrderCache(OrderEntity orderEntity,Boolean switchFlag){
        if(switchFlag) {
            redisUtil.set(buildOrderKey(orderEntity), orderEntity,TIMEOUT, TimeUnit.DAYS);
        }
        //获取开关配置，是否写ES
        if(esFlag){
            System.out.println("数据同步写入es");
            orderEsService.saveOrderToEs(orderEntity);
            //发送补偿落库mq
            System.out.println("发送补偿MQ");
        }
    }

    public void delOrderCache(OrderEntity orderEntity,Integer result){
        if(result > 0) {
            redisUtil.del(buildOrderKey(orderEntity));
        }
        if(esFlag){
            orderEsService.updateOrderToEs(orderEntity);
            //发送补偿mq
            System.out.println("发送补偿MQ");
        }
    }

    public OrderEntity getOrderCache(OrderEntity orderEntity){
        Object value = redisUtil.get(buildOrderKey(orderEntity));
        if(null != value){
            System.out.println("缓存查询单条数据");
            return JSON.parseObject(value.toString(), OrderEntity.class);
        }
        //获取开关配置，查询Es
        if(esFlag){
            //查询ES
            System.out.println("单条信息降级查询ES");
            return orderEsService.queryOrderInfoFromEs(orderEntity);
        }
        return null;
    }

    public List<OrderEntity> queryOrderPageCache(Page<OrderEntity> page, String userPin){
        //查询缓存
        Object value = redisUtil.get(buildPageKey(page, userPin));
        if(null != value){
            System.out.println("分页缓存数据返回");
            return (List<OrderEntity>)value;
        }
        //查询ES
        return orderEsService.queryOrderPageFromEs(page,buildOrderEntity(userPin));
    }

    private OrderEntity buildOrderEntity(String userPin) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUserPin(userPin);
        return orderEntity;
    }

    public void saveOrderPageCache(Page<OrderEntity> page, String userPin){
        System.out.println("保存分页数据缓存");
        redisUtil.set(buildPageKey(page,userPin),page.getRecords(),TIMEOUT, TimeUnit.SECONDS);
    }

    private String buildPageKey(Page<OrderEntity> page, String userPin) {
        return String.format("%s%s%s%s",PAGE_PREFIX,userPin,page.getCurrent(),page.getSize());
    }

    private String buildOrderKey(OrderEntity orderEntity) {
        return String.format("%s%s",ORDER_KEY,orderEntity.getOrderId());
    }


    public static void main(String[] args) {
        OrderEntity orderEntity1 = new OrderEntity();
        orderEntity1.setUserPin("123");
        orderEntity1.setOrderStatus(1);

        OrderEntity orderEntity2 = new OrderEntity();
        orderEntity2.setUserPin("123");
        orderEntity2.setOrderStatus(2);
        orderEntity2.setOrderId(System.currentTimeMillis());
        System.out.println(JSONUtil.toJsonStr(orderEntity1));
        System.out.println(JSONUtil.toJsonStr(orderEntity2));
        BeanUtil.copyProperties(orderEntity2,orderEntity1);

        System.out.println(JSONUtil.toJsonStr(orderEntity1));
        System.out.println(JSONUtil.toJsonStr(orderEntity2));
    }

}
