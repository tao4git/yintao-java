package com.java.yintao.order.facade;

import cn.hutool.json.JSONUtil;
import com.java.yintao.order.base.appoint.AppointCacheService;
import com.java.yintao.order.base.order.OrderCacheService;
import com.java.yintao.order.domain.entity.AppointEntity;
import com.java.yintao.order.domain.entity.OrderEntity;
import com.java.yintao.order.export.dto.OrderDto;
import com.java.yintao.order.export.param.OrderInfoQueryParam;
import com.java.yintao.order.export.param.OrderListQueryParam;
import com.java.yintao.order.export.param.OrderSaveParam;
import com.java.yintao.order.export.param.OrderSubmitParam;
import com.java.yintao.order.export.service.OrderExportService;
import com.java.yintao.order.service.impl.OrderManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yintao
 * @desc 写清楚这个类的作用
 * @package com.java.yintao.order.facade
 * @date 2021/3/2816:22
 */
@Service("orderExportService")
@Slf4j
public class OrderExportServiceImpl implements OrderExportService {

//    @Autowired
//    OrderBaseService orderBaseService;
//
//    @Autowired
//    AppointBaseService appointBaseService;

    @Autowired
    OrderManagerService orderManagerService;

    @Autowired
    OrderCacheService orderCacheService;

    @Autowired
    AppointCacheService appointCacheService;

    @Value("${mysql.to.es.flag}")
    private Boolean esFlag;

    /**
     * 模拟拆单消息,解决事务问题
     * @param orderSaveParam
     */
    @Override
    public void saveOrder(OrderSaveParam orderSaveParam) {
        try {
            //组装订单相关参数
            OrderEntity orderEntity = initOrder(orderSaveParam);
            log.info("saveOrder initOrder result ={}", JSONUtil.toJsonStr(orderEntity));
            //组装预约单数据
            List<AppointEntity> appointEntityList = initAppoint(orderSaveParam);
            log.info("saveOrder initAppoint result ={}", JSONUtil.toJsonStr(appointEntityList));
            Boolean result = Boolean.FALSE;
            try {
                //保障是一个事务性的插入语句
                result = orderManagerService.saveOrderAndAppoint(orderEntity, appointEntityList);
            }catch (Exception e){
                log.error("数据库插入异常....");
            }
            //插入成功或者ES开关打开
            Boolean switchFlag = result || esFlag ;
            if(!switchFlag){
                log.info("未切换ES数据源,返回错误信息");
                throw new Exception("数据库开小差了,请稍后重试");
            }
            orderCacheService.saveOrderCache(orderEntity,switchFlag);
            for (int i = 0; i < appointEntityList.size(); i++) {
                appointCacheService.saveAppointCache(appointEntityList.get(i),switchFlag);
            }
            //发送入库完成消息，便于后续进行发送短信通知
            log.info("入库完成,发送MQ消息");
        }catch (Exception e){
            log.info("保存订单异常...msg={}",e.getMessage());
        }
    }

    /**
     * 自营订单
     * @param orderSaveParam
     * @return
     * @throws Exception
     */
    private List<AppointEntity> initAppoint(OrderSaveParam orderSaveParam) throws Exception {
        List<AppointEntity> list = new ArrayList<>(8);
        if(orderSaveParam.getSkuNum() <= 0){
            throw new Exception("订单数量为空");
        }
        for (int i = 0; i < orderSaveParam.getSkuNum(); i++) {
            AppointEntity appointEntity = new AppointEntity();
            appointEntity.setUserPin(orderSaveParam.getUserPin());
            appointEntity.setOrderId(Long.valueOf(orderSaveParam.getOrderId()));
            appointEntity.setJdAppointmentId(System.currentTimeMillis()+i);
            appointEntity.setUserOrderPhone(orderSaveParam.getUserPhone());
            appointEntity.setOrderStatus(0);
            appointEntity.setSkuNo(orderSaveParam.getSkuNo());
            appointEntity.setSkuName(orderSaveParam.getSkuName());
            appointEntity.setSkuAmount(orderSaveParam.getOrderAmount());
            appointEntity.setOrderAppointType(0);
            appointEntity.setAppointSource(0);
            //调用RPC获取商品过期时间
//            appointEntity.setExpireTime(new Date());
            list.add(appointEntity);
        }
        return list;
    }

    private OrderEntity initOrder(OrderSaveParam orderSaveParam) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(Long.valueOf(orderSaveParam.getOrderId()));
        orderEntity.setUserPin(orderSaveParam.getUserPin());
        orderEntity.setParentOrderId(Long.valueOf(orderSaveParam.getOrderId()));
        orderEntity.setPayWay(orderSaveParam.getPayWay());
        orderEntity.setOrderAmount(orderSaveParam.getOrderAmount());
        orderEntity.setOrderTotalAmount(orderSaveParam.getTotalAmount());
        orderEntity.setOrderDiscount(orderSaveParam.getDiscoutAmount());
        orderEntity.setOrderStatus(0);
        orderEntity.setOrderType("200");
        orderEntity.setOrderAppointType(0);
        orderEntity.setSkuNum(orderSaveParam.getSkuNum());
        orderEntity.setSkuName(orderSaveParam.getSkuName());
        orderEntity.setSkuNo(orderSaveParam.getSkuNo());
        orderEntity.setUserPhone(orderSaveParam.getUserPhone());
        return orderEntity;
    }

    @Override
    public OrderDto submitOrder(OrderSubmitParam orderSubmitParam) {
        return null;
    }

    @Override
    public OrderDto queryOrderInfo(OrderInfoQueryParam orderInfoQueryParam) {
        return null;
    }

    @Override
    public List<OrderDto> queryOrderList(OrderListQueryParam orderListQueryParam) {
        return null;
    }
}
