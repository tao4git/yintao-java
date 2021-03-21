package com.java.yintao.order;

import com.java.yintao.order.domain.entity.OrderEntity;
import com.java.yintao.order.mapper.OrderMapper;
import com.java.yintao.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
class YintaoOrderApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    OrderService orderService;

    @Test
    public void queryInfoTest(){
        OrderEntity orderEntity = new OrderEntity();
        Long orderId = System.currentTimeMillis();
        orderEntity.setOrderId(orderId);
//        orderEntity.setParentOrderId(orderId);
//        orderEntity.setUserName("test");
//        orderEntity.setUserPhone("16619887367");
//        orderEntity.setUserPin("yintao");
//        orderEntity.setOrderTotalAmount(new BigDecimal("10.21"));
//        orderEntity.setOrderAmount(new BigDecimal("10.21"));
//        orderEntity.setOrderDiscount(new BigDecimal("0"));
        orderService.insert(orderEntity);
    }

}
