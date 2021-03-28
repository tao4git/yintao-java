package com.java.yintao.order.export.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yintao
 * @desc 写清楚这个类的作用
 * @package com.java.yintao.order.export.dto
 * @date 2021/3/2816:02
 */
@Data
public class OrderDto {

    private String orderId;

    private String userPin;

    private String userName;

    private Integer orderStatus;

    private String userPhone;

    private String userAddress;

    private Date payTime;

    private Integer payWay;

    private Integer orderType;

    private BigDecimal totalAmount;

    private BigDecimal orderAmount;

    private BigDecimal discoutAmount;
}
