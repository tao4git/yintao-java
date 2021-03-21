package com.java.yintao.order.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yintao
 * @desc 写清楚这个类的作用
 * @package com.java.yintao.order.domain.entity
 * @date 2021/3/21 21:02
 */
@TableName("my_order")
@Data
public class OrderEntity {

    @TableId(type = IdType.NONE)
    private Long id;

    private Long orderId;

    private Long parentOrderId;

    private String userPin;

    private String userName;

    private String userPhone;

    private String userAddress;

    private Integer orderStatus;

    private Date orderTime;

    private BigDecimal orderTotalAmount;

    private BigDecimal orderAmount;

    private BigDecimal orderDiscount;

    private int payStatus;

    private Date payTime;

    private Integer payWay;

    private int orderType;

    private int yn;

    private Date createTime;

    private Date updateTime;


}
