package com.java.yintao.order;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yintao.order.domain.entity.AppointEntity;
import com.java.yintao.order.service.AppointBaseService;
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
public class AppointTest {


    @Autowired
    AppointBaseService appointBaseService;

    @Test
    public void normalTest(){
        //插入
        Long orderId = System.currentTimeMillis();
        String userPin = "yintao";
        System.out.println("orderId:"+orderId);
        Integer insertResult = appointBaseService.insert(buildAppoint(orderId));
        if(insertResult > 0) {
            System.out.println("插入数据成功........");
        }else{
            System.out.println("插入数据库失败....");
        }
        //查询单条
        System.out.println("查询单条1："+JSONUtil.toJsonStr(appointBaseService.queryAppointInfoByPin(userPin,orderId)));
        //查询分页
        System.out.println("查询分页1："+JSONUtil.toJsonStr(appointBaseService.queryAppointPageByPin(new Page<>(),userPin)));
        //更新
        Integer result = appointBaseService.updateAppointStatus(buildUpdateAppoint());
        System.out.println("-----------更新数据--------------------"+result);
        //查询单条
        System.out.println("查询单条2："+JSONUtil.toJsonStr(appointBaseService.queryAppointInfoByPin(userPin,orderId)));
        //查询分页
        System.out.println("查询分页2："+JSONUtil.toJsonStr(appointBaseService.queryAppointPageByPin(new Page<>(),userPin)));
    }

    private AppointEntity buildUpdateAppoint() {
        AppointEntity appointEntity = new AppointEntity();
        appointEntity.setUserPin("yintao");
        appointEntity.setJdAppointmentId(1616932329233L);
        appointEntity.setOrderStatus(2);
        appointEntity.setStoreId("123");
        appointEntity.setStoreName("测试门店");
        appointEntity.setUserName("阿斯蒂芬");
        appointEntity.setUserOrderPhone("15234234");
        appointEntity.setIdCardType(1);
        appointEntity.setIdCardNo("4320234234");
        appointEntity.setAppointDate(new Date());
        return appointEntity;
    }

    @Test
    public void testInsertAppoint(){
        Long jdId = System.currentTimeMillis();
        appointBaseService.insert(buildAppoint(jdId));
    }

    private AppointEntity buildAppoint(Long jdAppointmentId) {
        AppointEntity appointEntity = new AppointEntity();
        appointEntity.setOrderId(1616917600108L);
        appointEntity.setUserPin("yintao");
        appointEntity.setJdAppointmentId(jdAppointmentId);
        appointEntity.setOrderStatus(0);
        appointEntity.setAppointStatus(0);
        appointEntity.setAppointSource(0);
        appointEntity.setOrderAppointType(0);
        appointEntity.setSkuNo("123");
        appointEntity.setSkuName("测试商品");
        appointEntity.setEnterpriseCostType(0);
        appointEntity.setSkuAmount(new BigDecimal("11.20"));
        appointEntity.setUserOrderPhone("16619887367");
        appointEntity.setUserName("银涛");
        return appointEntity;
    }

    @Test
    public void updateAppointStatus(){
        Integer result = appointBaseService.updateAppointStatus(buildUpdateAppoint());
        System.out.println(result);
    }

    @Test
    public void queryAppointById(){
        AppointEntity appointEntity = appointBaseService.queryAppointInfoByPin("yintao", 1616932329233L);
        System.out.println(appointEntity);
    }

    @Test
    public void queryAppointPage(){
        Page<AppointEntity> page = appointBaseService.queryAppointPageByPin(new Page<>(), "yintao");
        System.out.println(JSON.toJSONString(page));
    }

}
