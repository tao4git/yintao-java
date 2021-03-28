package com.java.yintao.order;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yintao.order.domain.entity.OrderEntity;
import com.java.yintao.order.service.OrderBaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yintao
 * @desc 写清楚这个类的作用
 * @package com.java.yintao.order
 * @date 2021/3/27 22:29
 * 基础设计方案
 * 1、列表查询：从ES+Redis（短暂）
 * 2、订单详情: Redis+es
 * 3、下单操作：库，写缓存、binlake更新ES和缓存覆盖
 * 4、订单更新：库，删缓存、binlake更新ES和缓存覆盖
 *
 * binlake是否需要反查，不查有binlake非顺序消息问题
 * 查询的话
 * 1、首选走数据库，单条查询性能OK，但如果数据库挂掉怎么解决？ 自动降级，不反查，推荐
 * 2、更新操作时同步更新缓存，非删除缓存，全量缓存，代码复杂度提高
 * 3、使用顺序消息，性能急剧下降，大流量下数据一致性时延较大
 *
 * 1、数据库故障，无法读写
 * 列表查询无影响
 * 订单详情无影响
 * 下单有影响，无法写库
 * 更新有影响，无法写库
 * 降级措施：
 * 下单：单独写缓存和ES，发送补偿MQ，库恢复后，会重新发送binlake，自动更新
 * 更新：有问题（删缓存），更新ES，发送补偿MQ
 * 2、ES故障，无法读写
 * 2.1 切换ES集群（需要双写），核心系统推荐
 * 2.2 切数据库查询，无法满足分词检索场景
 * 3、REDIS故障，无法读写
 * 捕获REDIS异常，走ES查询
 */
@SpringBootTest
public class OrderTest {


    @Autowired
    OrderBaseService orderBaseService;

    @Test
    public void normalTest(){
        //插入
        Long orderId = System.currentTimeMillis();
        String userPin = "yintao";
        System.out.println("orderId:"+orderId);
        Integer insertResult = orderBaseService.insert(buildOrder(orderId));
        if(insertResult > 0) {
            System.out.println("插入数据成功........");
        }else{
            System.out.println("插入数据库失败....");
        }
        //查询单条
        System.out.println("查询单条1："+JSONUtil.toJsonStr(orderBaseService.queryOrderInfoByPin(userPin,orderId)));
        //查询分页
        System.out.println("查询分页1："+JSONUtil.toJsonStr(orderBaseService.queryOrderPageByPin(new Page<>(),userPin)));
        //更新
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUserPin(userPin);
        orderEntity.setOrderId(orderId);
        orderEntity.setOrderStatus(2);
        orderEntity.setUserPhone("12321");
        orderEntity.setPayTime(new Date());
        Integer result = orderBaseService.updateOrderStatus(orderEntity);
        System.out.println("-----------更新数据--------------------"+result);
        //查询单条
        System.out.println("查询单条2："+JSONUtil.toJsonStr(orderBaseService.queryOrderInfoByPin(userPin,orderId)));
        //查询分页
        System.out.println("查询分页2："+JSONUtil.toJsonStr(orderBaseService.queryOrderPageByPin(new Page<>(),userPin)));
    }

    @Test
    public void testInsertOrder(){
        Long orderId = System.currentTimeMillis();
        orderBaseService.insert(buildOrder(orderId));
    }

    private OrderEntity buildOrder(Long orderId) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(orderId);
        orderEntity.setParentOrderId(orderId);
        orderEntity.setUserPin("yintao");
        orderEntity.setOrderAmount(new BigDecimal("12.11"));
        orderEntity.setOrderStatus(1);
        orderEntity.setUserPhone("16619887367");
        orderEntity.setPayWay(1);
        orderEntity.setSkuNo("123456");
        orderEntity.setSkuName("测试商品");
        orderEntity.setSkuNum(1);
        orderEntity.setPayTime(new Date());
        return orderEntity;
    }

    @Test
    public void updateOrderStatus(){
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUserPin("yintao");
        orderEntity.setOrderId(1616896952190L);
        orderEntity.setOrderStatus(2);
        Integer result = orderBaseService.updateOrderStatus(orderEntity);
        System.out.println(result);
    }

    @Test
    public void queryOrderById(){
        OrderEntity orderEntity = orderBaseService.queryOrderInfoByPin("yintao", 1616896952190L);
        System.out.println(orderEntity);
    }

    @Test
    public void queryOrderPage(){
        Page<OrderEntity> page = orderBaseService.queryOrderPageByPin(new Page<>(), "yintao");
        System.out.println(JSON.toJSONString(page));
    }

}
