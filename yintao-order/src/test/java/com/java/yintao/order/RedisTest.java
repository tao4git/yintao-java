package com.java.yintao.order;

import com.java.yintao.order.config.RedisUtil;
import com.java.yintao.order.domain.entity.OrderEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author yintao
 * @desc 写清楚这个类的作用
 * @package com.java.yintao.order
 * @date 2021/3/240:49
 */
@SpringBootTest
public class RedisTest {


//    @Autowired
//    RedissonClient redissonClient;

    @Autowired
    RedisUtil redisUtil;

    @Test
    public void testRedis(){
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUserPin("yintao");
        orderEntity.setOrderId(System.currentTimeMillis());
        redisUtil.set("test",orderEntity);
        System.out.println(redisUtil.get("test"));
    }

}
