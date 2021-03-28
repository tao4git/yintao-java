package com.java.yintao.order.export.param;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yintao
 * @desc 写清楚这个类的作用
 * @package com.java.yintao.order.export.param
 * @date 2021/3/2816:07
 */
@Data
public class OrderSubmitParam {

    private String orderId;

    private String userPin;

    private String userName;

    private String userPhone;

    private String userAddress;

    private Date payTime;

    private Integer payWay;

    private Integer orderType;

    private BigDecimal totalAmount;

    private BigDecimal orderAmount;

    private BigDecimal discoutAmount;
}
