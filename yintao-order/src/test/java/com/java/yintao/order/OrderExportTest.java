package com.java.yintao.order;

import com.java.yintao.order.export.param.OrderSaveParam;
import com.java.yintao.order.export.service.OrderExportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yintao
 * @desc 写清楚这个类的作用
 * @package com.java.yintao.order
 * @date 2021/3/2822:34
 */
@SpringBootTest
public class OrderExportTest {

    @Autowired
    OrderExportService orderExportService;

    @Test
    public void saveOrderAppointTest(){
        Long orderId = System.currentTimeMillis();
        System.out.println("orderId:"+orderId);
        orderExportService.saveOrder(buildOrderParam(orderId));
    }

    private OrderSaveParam buildOrderParam(Long orderId) {
        OrderSaveParam orderSaveParam = new OrderSaveParam();
        orderSaveParam.setUserPin("yintao");
        orderSaveParam.setOrderId(String.valueOf(orderId));
        orderSaveParam.setOrderAmount(new BigDecimal("12.11"));
        orderSaveParam.setTotalAmount(new BigDecimal("24.22"));
        orderSaveParam.setUserPhone("16619887367");
        orderSaveParam.setUserName("银涛");
        orderSaveParam.setUserAddress("北京市大兴区旧宫镇12号");
        orderSaveParam.setPayWay(1);
        orderSaveParam.setOrderType(200);
        orderSaveParam.setPayTime(new Date());
        orderSaveParam.setSkuNo("123456");
        orderSaveParam.setSkuNum(2);
        orderSaveParam.setSkuName("测试体检商品");
        return orderSaveParam;
    }
}
