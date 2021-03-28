package com.java.yintao.order.facade;

import com.java.yintao.order.export.dto.OrderDto;
import com.java.yintao.order.export.param.OrderInfoQueryParam;
import com.java.yintao.order.export.param.OrderListQueryParam;
import com.java.yintao.order.export.param.OrderSubmitParam;
import com.java.yintao.order.export.service.OrderExportService;
import com.java.yintao.order.service.OrderBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yintao
 * @desc 写清楚这个类的作用
 * @package com.java.yintao.order.facade
 * @date 2021/3/2816:22
 */
@Service("orderExportService")
public class OrderExportServiceImpl implements OrderExportService {

    @Autowired
    OrderBaseService orderBaseService;

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
