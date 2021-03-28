package com.java.yintao.order.export.service;

import com.java.yintao.order.export.dto.OrderDto;
import com.java.yintao.order.export.param.OrderInfoQueryParam;
import com.java.yintao.order.export.param.OrderListQueryParam;
import com.java.yintao.order.export.param.OrderSaveParam;
import com.java.yintao.order.export.param.OrderSubmitParam;

import java.util.List;

public interface OrderExportService {

    void saveOrder(OrderSaveParam orderSaveParam);

    OrderDto submitOrder(OrderSubmitParam orderSubmitParam);

    OrderDto queryOrderInfo(OrderInfoQueryParam orderInfoQueryParam);

    List<OrderDto> queryOrderList(OrderListQueryParam orderListQueryParam);


}
