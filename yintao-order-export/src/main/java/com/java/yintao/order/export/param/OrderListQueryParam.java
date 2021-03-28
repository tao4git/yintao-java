package com.java.yintao.order.export.param;

import lombok.Data;

/**
 * @author yintao
 * @desc 写清楚这个类的作用
 * @package com.java.yintao.order.export.param
 * @date 2021/3/2816:02
 */
@Data
public class OrderListQueryParam {

    private String userPin;

    private Integer orderStatus;
}
