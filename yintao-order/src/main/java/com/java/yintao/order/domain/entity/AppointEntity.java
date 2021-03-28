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
 * @date 2021/3/2816:36
 */
@TableName("my_appoint_info")
@Data
public class AppointEntity {

    @TableId(type = IdType.NONE)
    private Long id;

    private String userPin;

    private Long orderId;

    private Long jdAppointmentId;

    private String appointmentNo;
    /**
     * 业务类型
     */
    private Integer orderAppointType;

    private Integer appointSource;

    private Integer appointStatus;

    private Integer orderStatus;

    private String skuNo;

    private String skuName;

    private String skuAmount;
    /**
     * 门店结算价格
     */
    private BigDecimal storePayPrice;

    private String userName;

    private String userPhone;

    private Integer idCardType;

    private String idCardNo;

    private String storeId;

    private String storeName;

    private String goodsId;

    private Date appointDate;

    private Integer checkStatus;

    private Date checkDate;

    private Integer reportStatus;

    private Date reportDate;

    private Integer writeOffStatus;

    private Date writeOffTime;

    private Date exprieTime;

    /**
     * 是否升级套餐
     */
    private Integer enterpriseCostType;

    private String companyNo;

    private Long userId;

    private int yn;

    private Date createTime;

    private Date updateTime;


}
